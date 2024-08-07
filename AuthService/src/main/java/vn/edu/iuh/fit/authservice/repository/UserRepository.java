package vn.edu.iuh.fit.authservice.repository;

import vn.edu.iuh.fit.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
