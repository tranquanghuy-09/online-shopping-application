package vn.edu.iuh.fit.productservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.productservice.models.ProductPrice;
import vn.edu.iuh.fit.productservice.pks.ProductPricePK;
import vn.edu.iuh.fit.productservice.repositories.ProductPriceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductPriceService {
    @Autowired
    private ProductPriceRepository productPriceRepository;

    public List<ProductPrice> findAll() {
        return productPriceRepository.findAll();
    }

    public Optional<ProductPrice> findById(ProductPricePK id) {
        return productPriceRepository.findById(id);
    }

    public ProductPrice save(ProductPrice productPrice) {
        return productPriceRepository.save(productPrice);
    }

    public void deleteById(ProductPricePK id) {
        productPriceRepository.deleteById(id);
    }
}
