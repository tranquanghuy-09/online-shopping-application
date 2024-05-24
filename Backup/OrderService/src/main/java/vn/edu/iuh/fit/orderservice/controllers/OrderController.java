package vn.edu.iuh.fit.orderservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.orderservice.dto.OrderRequest;
import vn.edu.iuh.fit.orderservice.models.Order;
import vn.edu.iuh.fit.orderservice.services.OrderService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    @Autowired
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Placing Order");
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        log.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }

    //    @GetMapping
//    public List<Order> getAllOrders(){
//        return orderService.getOrders();
//    }
    @GetMapping
    public ResponseEntity<?> getAllOrders(HttpServletRequest request) throws JsonProcessingException {
        // Lấy token từ header của request
        String token = extractTokenFromRequest(request);
        System.out.println("Token: " + token);
        ResponseEntity<?> ordersViaToken = orderService.getOrdersViaToken(token);
        System.out.println("Orders: " + ordersViaToken);
        if (ordersViaToken != null) {
            return ordersViaToken;
        }
//    // Nếu token không null và hợp lệ
//    if (token != null && jwtService.isTokenValid(token)) {
//        // Lấy thông tin vai trò từ token
//        List<String> roles = jwtService.extractRoles(token);
//
//        // Kiểm tra quyền truy cập (ví dụ: kiểm tra nếu người dùng có vai trò 'admin' hay không)
//        if (roles.contains("admin")) {
//            // Trả về danh sách đơn hàng nếu người dùng có quyền
//            return orderService.getOrders();
//        } else {
//            // Nếu không có quyền, bạn có thể trả về một lỗi hoặc danh sách đơn hàng trống tùy thuộc vào logic của bạn
//            // Ví dụ: throw new AccessDeniedException("You do not have permission to access this resource");
//            // Hoặc: return Collections.emptyList();
//        }
//    }

        // Trả về null hoặc danh sách đơn hàng trống nếu không có quyền hoặc token không hợp lệ
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Lấy token từ header của request
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Nếu header Authorization không rỗng và có định dạng "Bearer <token>"
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Trả về phần token sau chuỗi "Bearer "
            return authHeader.substring(7);
        }

        return null;
    }
}
