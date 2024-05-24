package vn.edu.iuh.fit.orderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.edu.iuh.fit.orderservice.dto.InventoryResponse;
import vn.edu.iuh.fit.orderservice.dto.OrderLineItemsDto;
import vn.edu.iuh.fit.orderservice.dto.OrderRequest;
import vn.edu.iuh.fit.orderservice.dto.UserPrincipal;
import vn.edu.iuh.fit.orderservice.event.OrderPlacedEvent;
import vn.edu.iuh.fit.orderservice.models.Order;
import vn.edu.iuh.fit.orderservice.models.OrderLineItems;
import vn.edu.iuh.fit.orderservice.repositories.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders;
    }

    public Order getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    public String placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order: {}", orderRequest);
        Order order = new Order();
        String uuid = UUID.randomUUID().toString();
        order.setEmployeeId(orderRequest.getEmployeeId());
        order.setCustomerId(orderRequest.getCustomerId());
        order.setOrderDate(new Date());
        order.setOrderNumber(uuid);

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto, order))
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // Collect product IDs
        Set<Long> productIds = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getProductId)
                .collect(Collectors.toSet());

        // Check if all products exist
        for (Long productId : productIds) {
            if (!productExists(productId)) {
                throw new IllegalArgumentException("Product with ID " + productId + " does not exist");
            }
        }

        Map<String, Integer> skuCodeQuantities = order.getOrderLineItemsList().stream()
                .collect(Collectors.toMap(OrderLineItems::getSkuCode, OrderLineItems::getQuantity));

        // Build the URL for checking stock
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(inventoryServiceUrl + "/api/v1/inventories/check");
        String checkUrl = builder.build().toUriString();

        log.info("Inventory Check URL: {}", checkUrl);
        log.info("Checking Inventory for: {}", skuCodeQuantities);

        try {
            // Prepare headers and request entity
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(skuCodeQuantities, headers);

            // Send POST request to inventory service to check stock
            ResponseEntity<InventoryResponse[]> responseEntity = restTemplate.postForEntity(
                    checkUrl,
                    requestEntity,
                    InventoryResponse[].class
            );

            log.info("Inventory Response: {}", responseEntity);

            InventoryResponse[] inventoryResponseArray = responseEntity.getBody();

            // Check if all products are in stock and have sufficient quantity
            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                // Update the stock
                String updateUrl = UriComponentsBuilder.fromUriString(inventoryServiceUrl + "/api/v1/inventories/update").build().toUriString();
                log.info("Inventory Update URL: {}", updateUrl);
                restTemplate.postForEntity(updateUrl, requestEntity, Void.class);

                // Save the order
                orderRepository.save(order);

                // Publish Order Placed Event
                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                throw new IllegalArgumentException("Product is not in stock or insufficient quantity, please try again later");
            }
        } catch (Exception e) {
            log.error("Error while placing order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to place order due to inventory service error");
        }
    }

    private boolean productExists(Long productId) {
        try {
            String productUrl = productServiceUrl + productId;
            ResponseEntity<String> response = restTemplate.getForEntity(productUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Product with ID {} not found", productId);
            return false;
        } catch (Exception e) {
            log.error("Error while checking product existence: {}", e.getMessage(), e);
            throw new RuntimeException("Error while checking product existence");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto, Order order) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setProductId(orderLineItemsDto.getProductId());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setOrder(order);
        return orderLineItems;
    }

//    Cách làm cũ của HuyDev
//    public ResponseEntity<?> getOrdersViaToken(String token) throws JsonProcessingException {
//        if (token != null) {
//            // Tạo URL API với token
//            String apiUrl = "http://localhost:8181/api/v1/auth/validate-token?token=" + token;
//
//            // Gửi yêu cầu GET và nhận phản hồi
//            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
//            UserPrincipal userPrincipal = null;
//            try {
//                // Thử chuyển đổi phản hồi thành đối tượng UserPrincipal
//                userPrincipal = objectMapper.readValue(responseEntity.getBody(), UserPrincipal.class);
//                System.out.println(userPrincipal);
//            } catch (Exception e) {
//                // Xử lý ngoại lệ nếu không thể chuyển đổi phản hồi thành UserPrincipal
//                e.printStackTrace();
//            }
//
//            // Kiểm tra nếu userPrincipal null hoặc không có quyền
//            if (userPrincipal == null) {
//                // Trả về một mã lỗi HTTP thích hợp hoặc xử lý khác tùy thuộc vào yêu cầu của bạn
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
//            }
//
//            // Lấy danh sách đơn hàng từ cơ sở dữ liệu
//            List<Order> orders = orderRepository.findAll();
//            // Set orderLineItemsList to empty for each order
//            orders.forEach(order -> order.setOrderLineItemsList(Collections.emptyList()));
//            return ResponseEntity.ok(orders);
//
//        } else {
//            // Xử lý trường hợp không tìm thấy token trong request
//            System.out.println("Không tìm thấy token trong request.");
//            // Trả về một mã lỗi HTTP thích hợp hoặc xử lý khác tùy thuộc vào yêu cầu của bạn
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token not found");
//        }
//    }
}
