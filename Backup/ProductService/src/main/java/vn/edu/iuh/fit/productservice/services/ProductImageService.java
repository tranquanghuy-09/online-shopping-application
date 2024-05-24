package vn.edu.iuh.fit.productservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.productservice.models.ProductImage;
import vn.edu.iuh.fit.productservice.repositories.ProductImageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    public List<ProductImage> findAll() {
        return productImageRepository.findAll();
    }

    public Optional<ProductImage> findById(long id) {
        return productImageRepository.findById(id);
    }

    public ProductImage save(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    public void deleteById(long id) {
        productImageRepository.deleteById(id);
    }
}
