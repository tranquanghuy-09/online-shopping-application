package vn.edu.iuh.fit.orderservice.configs;

import io.github.bucket4j.Bandwidth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.iuh.fit.orderservice.models.reposiotory.UserPlan;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum PricingPlan {
  FREE(10),
  BASIC(40),
  PROFESSIONAL(100);

  private final int bucketCapacity;

  public Bandwidth getLimit() {
    return Bandwidth.builder()
        .capacity(bucketCapacity)
        .refillIntervally(bucketCapacity, Duration.ofMinutes(1))
        .build();
  }

  public static PricingPlan resolvePricingFromUserPlan(UserPlan userPlan) {
    if (UserPlan.BASIC.equals(userPlan)) {
      return BASIC;
    }
    if (UserPlan.PRO.equals(userPlan)) {
      return PROFESSIONAL;
    }
    return FREE;
  }
}
