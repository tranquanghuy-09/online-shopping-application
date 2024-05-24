package vn.edu.iuh.fit.orderservice.configs;

import vn.edu.iuh.fit.orderservice.configs.interceptor.ApiKeyRateLimitRequestInterceptor;
import vn.edu.iuh.fit.orderservice.configs.interceptor.IpRateLimitRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebRtConfig implements WebMvcConfigurer {

  @Autowired ApiKeyRateLimitRequestInterceptor apiKeyRateLimitRequestInterceptor;
  @Autowired IpRateLimitRequestInterceptor ipRateLimitRequestInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
//    registry
//        .addInterceptor(apiKeyRateLimitRequestInterceptor)
//        .addPathPatterns("/api/v1/orders/place-order");
    registry.addInterceptor(ipRateLimitRequestInterceptor).addPathPatterns("/api/v1/orders/place-order");
  }
}
