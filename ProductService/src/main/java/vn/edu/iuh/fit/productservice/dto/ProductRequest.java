package vn.edu.iuh.fit.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.productservice.enums.ProductStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private String unit;
    private String manufacturer;
    private ProductStatus status;
    private List<ProductImageRequest> productImages;
    private List<ProductPriceRequest> productPrices;

    private Integer quantity;
}