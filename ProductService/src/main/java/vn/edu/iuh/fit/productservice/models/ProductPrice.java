package vn.edu.iuh.fit.productservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.productservice.pks.ProductPricePK;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product_price")
@IdClass(ProductPricePK.class)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductPrice implements Serializable {
    @Id
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;
    @Id
    @Column(name = "price_date_time")
    private LocalDateTime price_date_time;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "note")
    private String note;

    @Override
    public String toString() {
        return "ProductPrice{" +
//                "product=" + product +
                ", price_date_time=" + price_date_time +
                ", price=" + price +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return Objects.equals(price_date_time, that.price_date_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price_date_time);
    }
}
