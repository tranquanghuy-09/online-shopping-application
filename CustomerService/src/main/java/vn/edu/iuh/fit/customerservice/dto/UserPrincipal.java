package vn.edu.iuh.fit.customerservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter @Setter @ToString
public class UserPrincipal {
    private Long userId;
    private String username;
    private String password;
    private Collection authorities;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
}
