package vn.edu.iuh.fit.productservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import vn.edu.iuh.fit.productservice.dto.InventoryDto;
import vn.edu.iuh.fit.productservice.enums.ProductStatus;
import vn.edu.iuh.fit.productservice.models.Product;
import vn.edu.iuh.fit.productservice.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String createInventoryUrl;

    public List<Product> findAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            String productsJson = jedis.get("products");
            if (productsJson != null) {
                return objectMapper.readValue(productsJson, new TypeReference<List<Product>>() {});
            } else {
                List<Product> products = productRepository.findAll();
                jedis.set("products", objectMapper.writeValueAsString(products));
                log.info("[HuyDev-LOG] - Get products from Database");
                return products;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return productRepository.findAll();
        }
    }

    public Optional<Product> findById(long id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String productJson = jedis.get("product:" + id);
            if (productJson != null) {
                return Optional.of(objectMapper.readValue(productJson, Product.class));
            } else {
                Optional<Product> product = productRepository.findById(id);
                product.ifPresent(p -> {
                    try {
                        jedis.set("product:" + id, objectMapper.writeValueAsString(p));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                log.info("[HuyDev-LOG] - Get products from Database");
                return product;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return productRepository.findById(id);
        }
    }

    public List<Product> findByStatus(ProductStatus status) {
        try (Jedis jedis = jedisPool.getResource()) {
            String productsJson = jedis.get("products:status:" + status);
            if (productsJson != null) {
                return objectMapper.readValue(productsJson, new TypeReference<List<Product>>() {});
            } else {
                List<Product> products = productRepository.findByStatus(status);
                jedis.set("products:status:" + status, objectMapper.writeValueAsString(products));
                return products;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return productRepository.findByStatus(status);
        }
    }

    private String generateSkuCode(String productName) {
        String[] parts = productName.split(" ");
        String skuCode = String.join("_", parts);
        return skuCode;
    }

    @Retry(name = "retryApiSaveInventory")
    public Product save(Product product, Integer quantity) {
        Product savedProduct = productRepository.save(product);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del("products");
            jedis.del("product:" + savedProduct.getProductId());
            jedis.del("products:status:" + savedProduct.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Gửi yêu cầu POST đến dịch vụ Inventory để tạo một mục mới trong kho
//        String createInventoryUrl = "http://localhost:8082/api/v1/inventories/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String skuCode = generateSkuCode(product.getName());
        InventoryDto request = new InventoryDto(savedProduct.getProductId(), skuCode, quantity);
        HttpEntity<InventoryDto> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(createInventoryUrl, requestEntity, Void.class);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            // Xử lý khi tạo thành công
            System.out.println("Inventory item created successfully");
        } else {
            // Xử lý khi gặp lỗi
            System.err.println("Failed to create inventory item. Status code: " + responseEntity.getStatusCodeValue());
        }

        return savedProduct;
    }

    public void deleteById(long id) {
        productRepository.deleteById(id);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del("products");
            jedis.del("product:" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
