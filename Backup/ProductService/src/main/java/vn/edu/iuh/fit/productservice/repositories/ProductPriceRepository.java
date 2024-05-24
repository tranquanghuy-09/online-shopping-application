package vn.edu.iuh.fit.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.productservice.models.ProductPrice;
import vn.edu.iuh.fit.productservice.pks.ProductPricePK;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, ProductPricePK> {
}
