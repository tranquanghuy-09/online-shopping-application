package vn.edu.iuh.fit.inventoryservice.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
    @Id
    private Long id;
    private String skuCode;
    private Integer quantity;
}
