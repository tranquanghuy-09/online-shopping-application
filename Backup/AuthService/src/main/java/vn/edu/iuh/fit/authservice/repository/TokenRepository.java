package vn.edu.iuh.fit.authservice.repository;

import vn.edu.iuh.fit.authservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
