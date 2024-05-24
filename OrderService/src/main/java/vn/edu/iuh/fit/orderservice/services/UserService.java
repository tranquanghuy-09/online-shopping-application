package vn.edu.iuh.fit.orderservice.services;


import vn.edu.iuh.fit.orderservice.models.reposiotory.RtUser;

public interface UserService {

  RtUser getById(String id);
  RtUser getByApiKey(String apikey);
  RtUser getByUsername(String username);
}
