package vn.edu.iuh.fit.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.inventoryservice.dto.InventoryResponse;
import vn.edu.iuh.fit.inventoryservice.models.Inventory;
import vn.edu.iuh.fit.inventoryservice.repositories.InventoryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceInStock {

    private final InventoryRepository inventoryRepository;

//    @Transactional(readOnly = true)
//    @SneakyThrows
//    public List<InventoryResponse> isInStock(Map<String, Integer> skuCodeQuantities) {
//        log.info("Checking Inventory");
//        List<String> skuCodes = skuCodeQuantities.keySet().stream().collect(Collectors.toList());
//
//        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
//                .map(inventory -> {
//                    int requestedQuantity = skuCodeQuantities.getOrDefault(inventory.getSkuCode(), 0);
//                    return InventoryResponse.builder()
//                            .skuCode(inventory.getSkuCode())
//                            .isInStock(inventory.getQuantity() >= requestedQuantity)
//                            .quantity(inventory.getQuantity())
//                            .requestedQuantity(requestedQuantity)
//                            .build();
//                }).collect(Collectors.toList());
//    }
//    public List<InventoryResponse> isInStock(List<String> skuCode) {
//        log.info("Checking Inventory");
//        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
//                .map(inventory ->
//                        InventoryResponse.builder()
//                                .skuCode(inventory.getSkuCode())
//                                .isInStock(inventory.getQuantity() > 0)
//                                .build()
//                ).toList();
//    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(Map<String, Integer> skuCodeQuantities) {
        log.info("Checking Inventory");
        List<String> skuCodes = skuCodeQuantities.keySet().stream().collect(Collectors.toList());

        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventory -> {
                    int requestedQuantity = skuCodeQuantities.getOrDefault(inventory.getSkuCode(), 0);
                    return InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() >= requestedQuantity)
                            .quantity(inventory.getQuantity())
                            .requestedQuantity(requestedQuantity)
                            .build();
                }).collect(Collectors.toList());
    }

    @Transactional
    @SneakyThrows
    public void updateStock(Map<String, Integer> skuCodeQuantities) {
        log.info("Updating Inventory");
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodeQuantities.keySet().stream().collect(Collectors.toList()));
        inventories.forEach(inventory -> {
            int requestedQuantity = skuCodeQuantities.getOrDefault(inventory.getSkuCode(), 0);
            inventory.setQuantity(inventory.getQuantity() - requestedQuantity);
        });
        inventoryRepository.saveAll(inventories);
    }
}
