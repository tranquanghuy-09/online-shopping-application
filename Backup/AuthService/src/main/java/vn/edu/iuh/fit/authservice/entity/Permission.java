package vn.edu.iuh.fit.authservice.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "t_permission")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseEntity {

    private String permissionName;

    private String permissionKey;

}