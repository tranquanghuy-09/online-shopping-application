package vn.edu.iuh.fit.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.orderservice.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
