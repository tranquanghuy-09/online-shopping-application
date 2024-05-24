package vn.edu.iuh.fit.productservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.edu.iuh.fit.productservice.services.AuthService;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private NoAuthenticationInterceptor noAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Thêm interceptor mới để bỏ qua việc xác thực cho API GET /api/v1/products và /api/v1/products/{id}
        registry.addInterceptor(noAuthenticationInterceptor)
                .addPathPatterns("/api/v1/products", "/api/v1/products/*");

        // Thêm interceptor cho các API cần xác thực
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/products/**");
    }
}

