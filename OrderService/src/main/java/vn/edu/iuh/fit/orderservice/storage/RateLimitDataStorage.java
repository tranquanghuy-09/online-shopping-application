package vn.edu.iuh.fit.orderservice.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RateLimitDataStorage {

  private static final Map<String, RateLimitData> STORAGE = new ConcurrentHashMap<>();

  private RateLimitDataStorage() {
    throw new RuntimeException();
  }

  public static RateLimitData addRateLimitData(String apiKey, RateLimitData rateLimitData) {
    return STORAGE.putIfAbsent(apiKey, rateLimitData);
  }

  public static RateLimitData getRateLimitData(String apiKey) {
    return STORAGE.get(apiKey);
  }

  public static RateLimitData removeRateLimitData(String apiKey) {
    return STORAGE.remove(apiKey);
  }
}
