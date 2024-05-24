package vn.edu.iuh.fit.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.employeeservice.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
