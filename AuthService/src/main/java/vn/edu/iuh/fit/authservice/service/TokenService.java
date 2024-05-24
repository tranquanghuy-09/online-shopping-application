package vn.edu.iuh.fit.authservice.service;

import vn.edu.iuh.fit.authservice.models.Token;

public interface TokenService {
    Token createToken(Token token);

    Token findByToken(String token);

}
