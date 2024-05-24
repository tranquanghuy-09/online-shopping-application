package vn.edu.iuh.fit.orderservice.services;

import io.github.bucket4j.Bucket;
import vn.edu.iuh.fit.orderservice.models.reposiotory.UserPlan;

public interface PricingPlanService {
  Bucket resolveBucketByUserPlan(UserPlan userPlan);
  Bucket resolveBucketByIp(String ipAddress);
}
