package vn.edu.iuh.fit.authservice.service;

import vn.edu.iuh.fit.authservice.authen.UserPrincipal;
import vn.edu.iuh.fit.authservice.dto.UserDTO;
import vn.edu.iuh.fit.authservice.models.User;

import java.util.Set;

public interface UserService {
    //    User createUser(User user);
    User createUser(UserDTO userDTO, Set<String> roleKeys);

    UserPrincipal findByUsername(String username);

    Long getUserIdFromToken(String token);

//    ResponseEntity<?> createEmployeeForUser(User user);
}
