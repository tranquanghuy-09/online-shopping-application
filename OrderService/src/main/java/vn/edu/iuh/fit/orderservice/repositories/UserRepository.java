package vn.edu.iuh.fit.orderservice.repositories;

import vn.edu.iuh.fit.orderservice.models.reposiotory.RtUser;

public interface UserRepository {

  RtUser getById(String uuid);
  RtUser getByApiKey(String apiKey);
  RtUser getByUsername(String username);
}
