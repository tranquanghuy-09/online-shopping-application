package vn.edu.iuh.fit.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceRequest {
    private double price;
    private LocalDateTime priceDateTime;
    private String note;
}