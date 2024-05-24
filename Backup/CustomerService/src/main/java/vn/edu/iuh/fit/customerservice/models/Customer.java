package vn.edu.iuh.fit.customerservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_customer")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "customer_id")
    private Long id;
    @Column(columnDefinition = "varchar(150)", name = "cust_name")
    private String fullName;
    @Column(columnDefinition = "varchar(15)")
    private String phone;
    @Column(columnDefinition = "varchar(250)")
    private String address;
    @Column(columnDefinition = "varchar(150)", unique = true)
    private String email;
}
