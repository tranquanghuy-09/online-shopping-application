package vn.edu.iuh.fit.authservice.repository;

import vn.edu.iuh.fit.authservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
