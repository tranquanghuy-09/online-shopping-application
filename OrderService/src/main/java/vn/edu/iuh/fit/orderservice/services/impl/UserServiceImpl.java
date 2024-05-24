package vn.edu.iuh.fit.orderservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.orderservice.models.reposiotory.RtUser;
import vn.edu.iuh.fit.orderservice.services.UserService;
import vn.edu.iuh.fit.orderservice.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public RtUser getById(String id) {
    return userRepository.getById(id);
  }

  public RtUser getByApiKey(String apiKey) {
    return userRepository.getByApiKey(apiKey);
  }

  public RtUser getByUsername(String username) {
    return userRepository.getByUsername(username);
  }
}
