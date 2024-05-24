package vn.edu.iuh.fit.authservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {
    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private String email;
}
