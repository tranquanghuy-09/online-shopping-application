package vn.edu.iuh.fit.orderservice.services.impl;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.orderservice.configs.PricingPlan;
import vn.edu.iuh.fit.orderservice.models.reposiotory.UserPlan;
import vn.edu.iuh.fit.orderservice.services.PricingPlanService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PricingPlanServiceImpl implements PricingPlanService {

  private final Map<UserPlan, Bucket> PLAN_BUCKETS = new ConcurrentHashMap<>();
  private final Map<String, Bucket> IP_BUCKETS = new ConcurrentHashMap<>();

  @Override
  public Bucket resolveBucketByUserPlan(UserPlan userPlan) {
    return PLAN_BUCKETS.computeIfAbsent(userPlan, this::newBucket);
  }

  @Override
  public Bucket resolveBucketByIp(String ipAddress) {
    return IP_BUCKETS.computeIfAbsent(ipAddress, userPlan -> newBucket(UserPlan.FREE));
  }

  private Bucket newBucket(UserPlan userPlan) {
    final PricingPlan pricingPlan = PricingPlan.resolvePricingFromUserPlan(userPlan);
    return Bucket.builder().addLimit(pricingPlan.getLimit()).build();
  }
}
