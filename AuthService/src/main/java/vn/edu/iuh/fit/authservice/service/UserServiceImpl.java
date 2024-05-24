package vn.edu.iuh.fit.authservice.service;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import vn.edu.iuh.fit.authservice.authen.UserPrincipal;
import vn.edu.iuh.fit.authservice.dto.CustomerDto;
import vn.edu.iuh.fit.authservice.dto.EmployeeDto;
import vn.edu.iuh.fit.authservice.dto.UserDTO;
import vn.edu.iuh.fit.authservice.models.Role;
import vn.edu.iuh.fit.authservice.models.User;
import vn.edu.iuh.fit.authservice.repository.RoleRepository;
import vn.edu.iuh.fit.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.authservice.util.JwtUtil;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${employee.service.url}")
    private String createEmployeeUrl;

    @Value("${customer.service.url}")
    private String createCustomerUrl;

    @Override
    @Retry(name = "retryApi")
    public User createUser(UserDTO userDTO, Set<String> roleKeys) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleKey : roleKeys) {
            Role role = roleRepository.findByRoleKey(roleKey);
            if (role != null) {
                roles.add(role);
            } else {
                // Optional: Handle case where role is not found
                throw new RuntimeException("Role not found: " + roleKey);
            }
        }
        user.setRoles(roles);


        if (roleKeys.contains("ROLE_USER")) {
            // If user has ROLE_USER, call createCustomerForUser
            User saved = userRepository.saveAndFlush(user);
            CustomerDto customer = CustomerDto.builder()
                    .id(saved.getId())
                    .fullName(userDTO.getFullName())
                    .email(userDTO.getEmail())
                    .phone(userDTO.getPhone())
                    .build();
            createCustomerForUser(customer);
            return saved;
        } else {
            User saved = userRepository.saveAndFlush(user);
            // Otherwise, call createEmployeeForUser
            EmployeeDto employee = EmployeeDto.builder()
                    .id(saved.getId())
                    .fullName(userDTO.getFullName())
                    .email(userDTO.getEmail())
                    .phone(userDTO.getPhone())
                    .status("1")
                    .build();
            createEmployeeForUser(employee);
            return saved;
        }
    }

    @Override
    public UserPrincipal findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal();

        if (null != user) {

            Set<String> authorities = new HashSet<>();

            if (null != user.getRoles())

                user.getRoles().forEach(r -> {
                    authorities.add(r.getRoleKey());
                    r.getPermissions().forEach(
                            p -> authorities.add(p.getPermissionKey()));
                });

            userPrincipal.setUserId(user.getId());
            userPrincipal.setUsername(user.getUsername());
            userPrincipal.setPassword(user.getPassword());
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }

    @Override
    public Long getUserIdFromToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);
        return user != null ? user.getId() : null;
    }

    //    @Override

    public void createEmployeeForUser(EmployeeDto employee) {
//        String createEmployeeUrl = "http://localhost:8085/api/v1/employees/register"; // URL của endpoint để tạo Employee

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeDto> request = new HttpEntity<>(employee, headers);

        ResponseEntity<EmployeeDto> responseEntity = restTemplate.postForEntity(createEmployeeUrl, request, EmployeeDto.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Nếu tạo thành công, bạn có thể lấy thông tin của nhân viên mới tạo
            EmployeeDto createdEmployee = responseEntity.getBody();
            System.out.println("Created employee: " + createdEmployee);
        } else {
            // Xử lý lỗi nếu có
            System.err.println("Failed to create employee. Status code: " + responseEntity.getStatusCodeValue());
        }
    }

    public void createCustomerForUser(CustomerDto customer) {
//        String createCustomerUrl = "http://localhost:8086/api/v1/customers/register"; // URL của endpoint để tạo Employee

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CustomerDto> request = new HttpEntity<>(customer, headers);

        ResponseEntity<CustomerDto> responseEntity = restTemplate.postForEntity(createCustomerUrl, request, CustomerDto.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            CustomerDto createdCustomer = responseEntity.getBody();
            System.out.println("Created customer: " + createdCustomer);
        } else {
            System.err.println("Failed to create customer. Status code: " + responseEntity.getStatusCodeValue());
        }
    }

}
