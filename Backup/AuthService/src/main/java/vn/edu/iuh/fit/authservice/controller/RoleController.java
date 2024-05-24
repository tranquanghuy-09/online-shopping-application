package vn.edu.iuh.fit.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.authservice.dto.RolePermissionsDTO;
import vn.edu.iuh.fit.authservice.entity.Role;
import vn.edu.iuh.fit.authservice.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('PERMISSION_WRITE')")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }
//    @PostMapping("/add-role-permissions")
//    @PreAuthorize("hasPermission('PERMISSION_WRITE')")
//    public Role addPermissionsToRole(@RequestBody RolePermissionsDTO rolePermissionsDTO) {
//        return roleService.createRoleWithPermissions(rolePermissionsDTO.getRoleKey(), rolePermissionsDTO.getPermissionKeys());
//    }

    @PostMapping("/add-permissions")
    @PreAuthorize("hasAnyAuthority('PERMISSION_WRITE')")
    public Role addPermissionsToRole(@RequestBody RolePermissionsDTO rolePermissionsDTO) {
        return roleService.addPermissionsToRole(rolePermissionsDTO.getRoleKey(), rolePermissionsDTO.getPermissionKeys());
    }
}
