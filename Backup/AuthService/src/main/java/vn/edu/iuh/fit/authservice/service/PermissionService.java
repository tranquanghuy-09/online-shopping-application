package vn.edu.iuh.fit.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.authservice.entity.Permission;
import vn.edu.iuh.fit.authservice.repository.PermissionRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        if (permissionRepository.findByPermissionKey(permission.getPermissionKey()) != null) {
            throw new RuntimeException("Permission already exists: " + permission.getPermissionKey());
        }
        return permissionRepository.save(permission);
    }

    public Permission findByPermissionKey(String permissionKey) {
        return permissionRepository.findByPermissionKey(permissionKey);
    }

    public List<Permission> createPermissions(List<Permission> permissions) {
        Set<String> existingPermissionKeys = permissionRepository.findAll()
                .stream()
                .map(Permission::getPermissionKey)
                .collect(Collectors.toSet());

        List<Permission> newPermissions = permissions.stream()
                .filter(permission -> !existingPermissionKeys.contains(permission.getPermissionKey()))
                .collect(Collectors.toList());

        return permissionRepository.saveAll(newPermissions);
    }
}
