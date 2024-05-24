package vn.edu.iuh.fit.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RolePermissionsDTO {
    private String roleKey;
    private Set<String> permissionKeys;
}
