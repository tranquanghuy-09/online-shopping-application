package vn.edu.iuh.fit.orderservice.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
public class RateLimitConfig {

  @Value("${huy.rate.limit.not.allowed.keys}") private List<String> notAllowedKeys;
  @Value("${huy.rate.limit.allowed.keys}")  private List<String> notAllowedIps;
}
