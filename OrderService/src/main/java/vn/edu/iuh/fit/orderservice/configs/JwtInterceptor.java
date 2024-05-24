package vn.edu.iuh.fit.orderservice.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.edu.iuh.fit.orderservice.dto.UserPrincipal;
import vn.edu.iuh.fit.orderservice.services.AuthService;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Autowired
    public JwtInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy token từ tiêu đề yêu cầu
        String token = extractTokenFromRequest(request);

        // Kiểm tra nếu không có token
        if (token == null) {
            // Trả về lỗi 401 Unauthorized
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not found");
            return false;
        }

        // Kiểm tra và xác thực token
        try {
            UserPrincipal user = authService.validateToken(token);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return false;
            }

            String requestURI = request.getRequestURI();
            String httpMethod = request.getMethod();

            // Kiểm tra quyền dựa trên phương thức và đường dẫn yêu cầu
            if (requestURI.matches("/api/v1/orders/\\d+")) { // Kiểm tra đường dẫn theo mẫu /api/v1/orders/{id}
                if ("GET".equalsIgnoreCase(httpMethod) && hasRole(user, "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER")) {
                    return true; // Người dùng có quyền USER, MANAGER hoặc ADMIN
                }
            } else if (requestURI.startsWith("/api/v1/orders")) {
                if ("GET".equalsIgnoreCase(httpMethod) && hasRole(user, "ROLE_ADMIN", "ROLE_MANAGER")) {
                    return true; // Người dùng có quyền ADMIN hoặc MANAGER cho GET /api/v1/orders
                } else if (!"GET".equalsIgnoreCase(httpMethod) && hasRole(user, "ROLE_USER")) {
                    return true; // Người dùng có quyền USER cho các phương thức khác
                } else if ("POST".equalsIgnoreCase(httpMethod) && hasRole(user, "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER")) {
                    return true; // Người dùng có quyền USER cho phương thức POST
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                    return false;
                }
            }

            // Nếu không có quyền phù hợp, trả về lỗi 403 Forbidden
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return false;
        } catch (HttpClientErrorException.BadRequest badRequest) {
            // Xử lý ngoại lệ khi token không hợp lệ
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }

    private boolean hasRole(UserPrincipal user, String... roles) {
        if (user != null && user.getAuthorities() != null) {
            for (Object authority : user.getAuthorities()) {
                String role = authority.toString();
                for (String requiredRole : roles) {
                    if (requiredRole.equals(role)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Lấy token từ header của request
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Nếu header Authorization không rỗng và có định dạng "Bearer <token>"
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Trả về phần token sau chuỗi "Bearer "
            return authHeader.substring(7);
        }

        return null;
    }
}
