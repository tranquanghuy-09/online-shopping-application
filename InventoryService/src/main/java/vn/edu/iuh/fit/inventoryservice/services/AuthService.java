package vn.edu.iuh.fit.inventoryservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.edu.iuh.fit.inventoryservice.models.UserPrincipal;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${auth.service.url}")
    private String authApiUrl;

    public UserPrincipal validateToken(String token) {
        System.out.println("Token: " + token);
        String apiUrl = authApiUrl + token;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        UserPrincipal userPrincipal = null;
        try {
            userPrincipal = objectMapper.readValue(responseEntity.getBody(), UserPrincipal.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPrincipal;
    }
}
