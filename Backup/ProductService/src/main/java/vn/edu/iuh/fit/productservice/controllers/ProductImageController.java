package vn.edu.iuh.fit.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.productservice.models.ProductImage;
import vn.edu.iuh.fit.productservice.services.ProductImageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {
    @Autowired
    private ProductImageService productImageService;

    @GetMapping
    public List<ProductImage> getAllProductImages() {
        return productImageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImage> getProductImageById(@PathVariable long id) {
        Optional<ProductImage> productImage = productImageService.findById(id);
        return productImage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductImage createProductImage(@RequestBody ProductImage productImage) {
        return productImageService.save(productImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImage> updateProductImage(@PathVariable long id, @RequestBody ProductImage productImageDetails) {
        Optional<ProductImage> productImageOptional = productImageService.findById(id);

        if (!productImageOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ProductImage productImage = productImageOptional.get();
        productImage.setPath(productImageDetails.getPath());
        productImage.setAlternative(productImageDetails.getAlternative());

        ProductImage updatedProductImage = productImageService.save(productImage);
        return ResponseEntity.ok(updatedProductImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable long id) {
        if (!productImageService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productImageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
