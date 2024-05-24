package vn.edu.iuh.fit.authservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private Set<String> roleKeys;
}
