package vn.edu.iuh.fit.customerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.customerservice.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
