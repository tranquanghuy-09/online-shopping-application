package vn.edu.iuh.fit.orderservice.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.orderservice.dto.OrderRequest;
import vn.edu.iuh.fit.orderservice.models.Order;
import vn.edu.iuh.fit.orderservice.services.OrderService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    @Autowired
    private final OrderService orderService;

    @PostMapping("/place-order")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    @RateLimiter(name = "inventoryService")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Placing Order");
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        log.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getOrders();
    }

//    @GetMapping("/test-rate-limiter")
//    public List<Order> getAllOrders2() {
//        long tokensToConsume = 1;
//        if (bucket.tryConsume(tokensToConsume)) {
//            return orderService.getOrders();
//        } else {
//            throw new RuntimeException("Too many requests - try again later");
//        }
//    }
//    Cách làm cũ của HuyDev
//    @GetMapping("/huy")
//    public ResponseEntity<?> getAllOrders(HttpServletRequest request) throws JsonProcessingException {
//        // Lấy token từ header của request
//        String token = extractTokenFromRequest(request);
//        System.out.println("Token: " + token);
//        ResponseEntity<?> ordersViaToken = orderService.getOrdersViaToken(token);
//        System.out.println("Orders: " + ordersViaToken);
//        if (ordersViaToken != null) {
//            return ordersViaToken;
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
//    }
//
//    private String extractTokenFromRequest(HttpServletRequest request) {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
//            return authHeader.substring(7);
//        }
//
//        return null;
//    }
}
