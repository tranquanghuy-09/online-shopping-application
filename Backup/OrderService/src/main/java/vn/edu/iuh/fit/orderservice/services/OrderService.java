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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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


    //    private final WebClient.Builder webClientBuilder;
//    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

//    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, WebClient.Builder webClientBuilder, ObservationRegistry observationRegistry, ApplicationEventPublisher applicationEventPublisher) {
//        this.orderRepository = orderRepository;
//        this.restTemplate = restTemplate;
//        this.webClientBuilder = webClientBuilder;
//        this.observationRegistry = observationRegistry;
//        this.applicationEventPublisher = applicationEventPublisher;
//    }

    public String placeOrder(OrderRequest orderRequest) {
        System.out.println(orderRequest);
        Order order = new Order();
        String uuid = UUID.randomUUID().toString();
        order.setOrderNumber(uuid);

//        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
//                .stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto, order))
                .collect(Collectors.toList());


        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        // Build the URL with multiple skuCodes as query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(inventoryServiceUrl + "/api/v1/inventory");
        System.out.println(skuCodes);
        skuCodes.forEach(skuCode -> builder.queryParam("skuCode", skuCode));
        String url = builder.build().toUriString();
        System.out.println(url);

        // Send GET request to inventory service
        ResponseEntity<InventoryResponse[]> responseEntity = restTemplate.getForEntity(
                url,
                InventoryResponse[].class
        );
        System.out.println(responseEntity);
        InventoryResponse[] inventoryResponseArray = responseEntity.getBody();

        // Check if all products are in stock
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
            // Publish Order Placed Event
            applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
            return "Order Placed";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

//    public String placeOrder(OrderRequest orderRequest) {
//        Order order = new Order();
//        order.setOrderNumber(UUID.randomUUID().toString());
//
//        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//
//        order.setOrderLineItemsList(orderLineItems);
//
//        List<String> skuCodes = order.getOrderLineItemsList().stream()
//                .map(OrderLineItems::getSkuCode)
//                .toList();
//
//        // Call Inventory Service, and place order if product is in
//        // stock
//        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
//                this.observationRegistry);
//        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
//        return inventoryServiceObservation.observe(() -> {
//            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
//                    .uri("http://inventory-service/api/v1/inventory",
//                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//                    .retrieve()
//                    .bodyToMono(InventoryResponse[].class)
//                    .block();
//
//            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
//                    .allMatch(InventoryResponse::isInStock);
//
//            if (allProductsInStock) {
//                orderRepository.save(order);
//                // publish Order Placed Event
//                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
//                return "Order Placed";
//            } else {
//                throw new IllegalArgumentException("Product is not in stock, please try again later");
//            }
//        });
//
//    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto, Order order) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setOrder(order);
        return orderLineItems;
    }

    public List<Order> getOrders() {

        List<Order> orders = orderRepository.findAll();
        // Set orderLineItemsList to empty for each order
        orders.forEach(order -> order.setOrderLineItemsList(Collections.emptyList()));
        return orders;
    }

    public ResponseEntity<?> getOrdersViaToken(String token) throws JsonProcessingException {
        if (token != null) {
            // Tạo URL API với token
            String apiUrl = "http://localhost:8181/api/v1/auth/validate-token?token=" + token;

            // Gửi yêu cầu GET và nhận phản hồi
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
            UserPrincipal userPrincipal = null;
            try {
                // Thử chuyển đổi phản hồi thành đối tượng UserPrincipal
                userPrincipal = objectMapper.readValue(responseEntity.getBody(), UserPrincipal.class);
                System.out.println(userPrincipal);
            } catch (Exception e) {
                // Xử lý ngoại lệ nếu không thể chuyển đổi phản hồi thành UserPrincipal
                e.printStackTrace();
            }

            // Kiểm tra nếu userPrincipal null hoặc không có quyền
            if (userPrincipal == null) {
                // Trả về một mã lỗi HTTP thích hợp hoặc xử lý khác tùy thuộc vào yêu cầu của bạn
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            // Lấy danh sách đơn hàng từ cơ sở dữ liệu
            List<Order> orders = orderRepository.findAll();
            // Set orderLineItemsList to empty for each order
            orders.forEach(order -> order.setOrderLineItemsList(Collections.emptyList()));
            return ResponseEntity.ok(orders);

        } else {
            // Xử lý trường hợp không tìm thấy token trong request
            System.out.println("Không tìm thấy token trong request.");
            // Trả về một mã lỗi HTTP thích hợp hoặc xử lý khác tùy thuộc vào yêu cầu của bạn
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token not found");
        }
    }


}
