package vn.edu.iuh.fit.authservice;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;
import vn.edu.iuh.fit.authservice.dto.UserDTO;
import vn.edu.iuh.fit.authservice.models.Permission;
import vn.edu.iuh.fit.authservice.models.Role;
import vn.edu.iuh.fit.authservice.service.PermissionService;
import vn.edu.iuh.fit.authservice.service.RoleService;
import vn.edu.iuh.fit.authservice.service.UserService;

import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
public class ServiceAuthProductApplication {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;

    private void initRoles() {
        if (roleService.findByRoleKey("ROLE_ADMIN") != null && roleService.findByRoleKey("ROLE_USER") != null && roleService.findByRoleKey("ROLE_MANAGER") != null){
            return;
        }
        roleService.createRole(Role.builder().roleKey("ROLE_ADMIN").roleName("Administrator").build());
        roleService.createRole(Role.builder().roleKey("ROLE_MANAGER").roleName("Manager").build());
        roleService.createRole(Role.builder().roleKey("ROLE_USER").roleName("User").build());
    }
    private void initPermissions() {
        if (permissionService.findByPermissionKey("PERMISSION_READ") != null && permissionService.findByPermissionKey("PERMISSION_WRITE") != null
                && permissionService.findByPermissionKey("PERMISSION_DELETE") != null && permissionService.findByPermissionKey("PERMISSION_UPDATE") != null){
            return;
        }
        permissionService.createPermission(Permission.builder().permissionKey("PERMISSION_READ").permissionName("Read Access All").build());
        permissionService.createPermission(Permission.builder().permissionKey("PERMISSION_WRITE").permissionName("Write Access All").build());
        permissionService.createPermission(Permission.builder().permissionKey("PERMISSION_DELETE").permissionName("Delete Access All").build());
        permissionService.createPermission(Permission.builder().permissionKey("PERMISSION_UPDATE").permissionName("Update Access All").build());
    }
    private void initRolePermissions() {
        if (!roleService.findByRoleKey("ROLE_ADMIN").getPermissions().isEmpty() && !roleService.findByRoleKey("ROLE_USER").getPermissions().isEmpty() && !roleService.findByRoleKey("ROLE_MANAGER").getPermissions().isEmpty()){
            return;
        }
        roleService.createRoleWithPermissions("ROLE_ADMIN", Set.of("PERMISSION_READ", "PERMISSION_WRITE", "PERMISSION_DELETE", "PERMISSION_UPDATE"));
        roleService.createRoleWithPermissions("ROLE_MANAGER", Set.of("PERMISSION_READ", "PERMISSION_WRITE", "PERMISSION_UPDATE"));
        roleService.createRoleWithPermissions("ROLE_USER", Set.of("PERMISSION_READ"));
    }

    private void initUsers() {
        UserDTO adminDTO = UserDTO.builder().username("admin").password("123").email("admin@email.com").fullName("Mr. Admin").phone("0000000000").roleKeys(Set.of("ROLE_ADMIN")).build();
        userService.createUser(adminDTO, adminDTO.getRoleKeys());

        UserDTO managerDTO = UserDTO.builder().username("manager").password("123").email("manager@email.com").fullName("Manager User").phone("1111111111").roleKeys(Set.of("ROLE_MANAGER")).build();
        userService.createUser(managerDTO, managerDTO.getRoleKeys());

        UserDTO userDTO = UserDTO.builder().username("user").password("123").email("user@email.com").fullName("Noob User").phone("2222222222").roleKeys(Set.of("ROLE_USER")).build();
        userService.createUser(userDTO, userDTO.getRoleKeys());
    }

    @PostConstruct
    public void init() {
        initPermissions();
        initRoles();
        initRolePermissions();
        initUsers();
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthProductApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
