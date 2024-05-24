package vn.edu.iuh.fit.productservice.controllers;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.productservice.dto.ProductRequest;
import vn.edu.iuh.fit.productservice.models.Product;
import vn.edu.iuh.fit.productservice.enums.ProductStatus;
import vn.edu.iuh.fit.productservice.models.ProductImage;
import vn.edu.iuh.fit.productservice.models.ProductPrice;
import vn.edu.iuh.fit.productservice.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PermitAll
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<Product> getProductsByStatus(@PathVariable ProductStatus status) {
        return productService.findByStatus(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody Product productDetails) {
        Optional<Product> productOptional = productService.findById(id);

        if (!productOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product product = productOptional.get();
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setUnit(productDetails.getUnit());
        product.setManufacturer(productDetails.getManufacturer());
        product.setStatus(productDetails.getStatus());

        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        if (!productService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody ProductRequest productRequest) {
        try {
            // Map từ ProductRequest sang Product entity
            Product product = mapToProduct(productRequest);

            // Lưu sản phẩm vào cơ sở dữ liệu
            Product savedProduct = productService.save(product);

            // Trả về thông tin sản phẩm đã lưu
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving product.");
        }
    }

    private Product mapToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setUnit(productRequest.getUnit());
        product.setManufacturer(productRequest.getManufacturer());
        product.setStatus(productRequest.getStatus());

        // Map danh sách hình ảnh từ ProductRequest sang ProductImage entities
        List<ProductImage> productImages = productRequest.getProductImages().stream()
                .map(image -> {
                    ProductImage productImage = new ProductImage();
                    productImage.setPath(image.getPath());
                    productImage.setAlternative(image.getAlternative());
                    productImage.setProduct(product); // Set product reference
                    return productImage;
                })
                .collect(Collectors.toList());

        // Map danh sách giá từ ProductRequest sang ProductPrice entities
        List<ProductPrice> productPrices = productRequest.getProductPrices().stream()
                .map(price -> {
                    ProductPrice productPrice = new ProductPrice();
                    productPrice.setPrice(price.getPrice());
                    productPrice.setPrice_date_time(price.getPriceDateTime());
                    productPrice.setNote(price.getNote());
                    productPrice.setProduct(product); // Set product reference
                    return productPrice;
                })
                .collect(Collectors.toList());

        product.setProductImageList(productImages);
        product.setProductPrices(productPrices);

        return product;
    }
}
