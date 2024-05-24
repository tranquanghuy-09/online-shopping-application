package vn.edu.iuh.fit.productservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vn.edu.iuh.fit.productservice.services.AuthService;

@Component
public class NoAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Autowired
    public NoAuthenticationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the request is a GET request for /api/v1/products or /api/v1/products/{id}
        String requestURI = request.getRequestURI();
        if ("GET".equals(request.getMethod()) &&
                (requestURI.matches("/api/v1/products") || requestURI.matches("/api/v1/products/\\d+"))) {
            // Allow access without authentication for GET /api/v1/products or GET /api/v1/products/{id}
            return true;
        }

        // For other requests, proceed with the normal authentication flow
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Your logic after the handler has been executed
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Your logic after the request has been completed
    }
}
