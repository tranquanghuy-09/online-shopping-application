package vn.edu.iuh.fit.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.authservice.entity.Permission;
import vn.edu.iuh.fit.authservice.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('PERMISSION_WRITE')")
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.createPermission(permission);
    }

    @PostMapping("/add-list")
    @PreAuthorize("hasAnyAuthority('PERMISSION_WRITE')")
    public List<Permission> createPermissions(@RequestBody List<Permission> permissions) {
        return permissionService.createPermissions(permissions);
    }
}
