package vn.edu.iuh.fit.customerservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.edu.iuh.fit.customerservice.dto.UserPrincipal;
import vn.edu.iuh.fit.customerservice.services.AuthService;

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
            if (isValidToken(token)) {
                // Token hợp lệ, cho phép xử lý yêu cầu tiếp theo
                return true;
            } else {
                // Token không hợp lệ, trả về lỗi 401 Unauthorized
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Không có quyền truy cập");
                return false;
            }
        } catch (HttpClientErrorException.BadRequest badRequest) {
            // Xử lý ngoại lệ khi token không hợp lệ
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }

    private boolean isValidToken(String token) {
        UserPrincipal user = authService.validateToken(token);
        System.out.println("User: " + user);
        if (user != null && user.getAuthorities() != null) {
            // Kiểm tra mỗi quyền trong danh sách authorities của người dùng
            for (Object authority : user.getAuthorities()) {
                String role = authority.toString();
                if ("ROLE_ADMIN".equals(role) || "ROLE_MANAGER".equals(role)) {
                    // Người dùng có vai trò ROLE_ADMIN và ROLE_MANAGER, token hợp lệ
                    return true;
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
