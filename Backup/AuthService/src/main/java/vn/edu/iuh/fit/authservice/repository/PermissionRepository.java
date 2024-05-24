package vn.edu.iuh.fit.authservice.repository;


import vn.edu.iuh.fit.authservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long>{
    Permission findByPermissionKey(String permissionKey);
}
