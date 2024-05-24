package vn.edu.iuh.fit.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.authservice.models.Permission;
import vn.edu.iuh.fit.authservice.models.Role;
import vn.edu.iuh.fit.authservice.repository.PermissionRepository;
import vn.edu.iuh.fit.authservice.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role createRoleWithPermissions(String roleKey, Set<String> permissionKeys) {
        Role role = roleRepository.findByRoleKey(roleKey);
        if (role == null) {
            throw new RuntimeException("Role not found: " + roleKey);
        }

        Set<Permission> permissions = new HashSet<>();
        for (String permissionKey : permissionKeys) {
            Permission permission = permissionRepository.findByPermissionKey(permissionKey);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public Role findByRoleKey(String roleKey) {
        return roleRepository.findByRoleKey(roleKey);
    }

    public Role addPermissionsToRole(String roleKey, Set<String> permissionKeys) {
        Role role = roleRepository.findByRoleKey(roleKey);
        if (role != null) {
            Set<Permission> existingPermissions = role.getPermissions();
            Set<Permission> newPermissions = new HashSet<>();
            for (String permissionKey : permissionKeys) {
                Permission permission = permissionRepository.findByPermissionKey(permissionKey);
                if (permission != null && !existingPermissions.contains(permission)) {
                    newPermissions.add(permission);
                }
            }
            role.getPermissions().addAll(newPermissions);
            return roleRepository.save(role);
        }
        return null;
    }
}
