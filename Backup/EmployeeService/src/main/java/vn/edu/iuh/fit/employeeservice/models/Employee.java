package vn.edu.iuh.fit.employeeservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_employee")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Employee {
    @Id
    @Column(name = "employee_id")
    private Long id;
    @Column(columnDefinition = "varchar(150)")
    private String fullName;
    @Column(columnDefinition = "varchar(150)", unique = true)
    private String email;
    @Column(columnDefinition = "varchar(15)")
    private String phone;
    @Column(columnDefinition = "varchar(250)")
    private String address;
    private String avatar;
    @Column(columnDefinition = "tinyint(1) default 1")
    private String status;
}
