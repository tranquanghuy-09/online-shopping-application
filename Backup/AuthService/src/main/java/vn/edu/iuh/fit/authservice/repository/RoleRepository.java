package vn.edu.iuh.fit.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.authservice.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleKey(String roleKey);
}
