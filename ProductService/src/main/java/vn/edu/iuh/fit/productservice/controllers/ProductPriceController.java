package vn.edu.iuh.fit.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.productservice.models.Product;
import vn.edu.iuh.fit.productservice.models.ProductPrice;
import vn.edu.iuh.fit.productservice.pks.ProductPricePK;
import vn.edu.iuh.fit.productservice.services.ProductPriceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-prices")
public class ProductPriceController {
    @Autowired
    private ProductPriceService productPriceService;

    @GetMapping
    public List<ProductPrice> getAllProductPrices() {
        return productPriceService.findAll();
    }

    @GetMapping("/{productId}/{priceDateTime}")
    public ResponseEntity<ProductPrice> getProductPriceById(@PathVariable long productId, @PathVariable String priceDateTime) {
        ProductPricePK id = new ProductPricePK();
        id.setProduct(new Product(productId));
        id.setPrice_date_time(LocalDateTime.parse(priceDateTime));
        Optional<ProductPrice> productPrice = productPriceService.findById(id);
        return productPrice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductPrice createProductPrice(@RequestBody ProductPrice productPrice) {
        return productPriceService.save(productPrice);
    }

    @PutMapping("/{productId}/{priceDateTime}")
    public ResponseEntity<ProductPrice> updateProductPrice(@PathVariable long productId, @PathVariable String priceDateTime, @RequestBody ProductPrice productPriceDetails) {
        ProductPricePK id = new ProductPricePK();
        id.setProduct(new Product(productId));
        id.setPrice_date_time(LocalDateTime.parse(priceDateTime));

        Optional<ProductPrice> productPriceOptional = productPriceService.findById(id);

        if (!productPriceOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ProductPrice productPrice = productPriceOptional.get();
        productPrice.setPrice(productPriceDetails.getPrice());
        productPrice.setNote(productPriceDetails.getNote());

        ProductPrice updatedProductPrice = productPriceService.save(productPrice);
        return ResponseEntity.ok(updatedProductPrice);
    }

    @DeleteMapping("/{productId}/{priceDateTime}")
    public ResponseEntity<Void> deleteProductPrice(@PathVariable long productId, @PathVariable String priceDateTime) {
        ProductPricePK id = new ProductPricePK();
        id.setProduct(new Product(productId));
        id.setPrice_date_time(LocalDateTime.parse(priceDateTime));

        if (!productPriceService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productPriceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
