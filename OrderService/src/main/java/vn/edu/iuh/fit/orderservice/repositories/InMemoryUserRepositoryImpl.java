package vn.edu.iuh.fit.orderservice.repositories;

import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.orderservice.models.reposiotory.RtUser;
import vn.edu.iuh.fit.orderservice.models.reposiotory.UserPlan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

  private static final Map<String, RtUser> STORAGE = new ConcurrentHashMap<>();

  static {
    STORAGE.put(
        "00001",
        new RtUser()
            .setUsername("FreeUser")
            .setApiKey("d0476978-free-4ad5-94e8-38ebb575f5c4")
            .setUserPlan(UserPlan.FREE));
    STORAGE.put(
        "00002",
        new RtUser()
            .setUsername("BasicUser")
            .setApiKey("d0476978-base-4ad5-94e8-38ebb575f5c5")
            .setUserPlan(UserPlan.BASIC));
    STORAGE.put(
        "00003",
        new RtUser()
            .setUsername("ProUser")
            .setApiKey("d0476978-prof-4ad5-94e8-38ebb575f5c6")
            .setUserPlan(UserPlan.PRO));
  }

  @Override
  public RtUser getById(String id) {
    return STORAGE.values().stream()
        .filter(rtUser -> id.equalsIgnoreCase(rtUser.getId()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("There is no user with id: " + id));
  }

  @Override
  public RtUser getByApiKey(String apiKey) {
    return STORAGE.values().stream()
        .filter(rtUser -> apiKey.equalsIgnoreCase(rtUser.getApiKey()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("There is no user with api key: " + apiKey));
  }

  @Override
  public RtUser getByUsername(String username) {
    return STORAGE.values().stream()
        .filter(rtUser -> username.equalsIgnoreCase(rtUser.getUsername()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("There is no user with username: " + username));
  }
}
