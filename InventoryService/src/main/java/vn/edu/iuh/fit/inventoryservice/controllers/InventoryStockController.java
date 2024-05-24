package vn.edu.iuh.fit.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.inventoryservice.dto.InventoryResponse;
import vn.edu.iuh.fit.inventoryservice.models.Inventory;
import vn.edu.iuh.fit.inventoryservice.services.InventoryService;
import vn.edu.iuh.fit.inventoryservice.services.InventoryServiceInStock;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryStockController {

    private final InventoryServiceInStock inventoryServiceInStock;

    @Autowired
    private InventoryService inventoryService;

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red

    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestBody Map<String, Integer> skuCodeQuantities) {
        log.info("Received inventory check request for skuCodeQuantities: {}", skuCodeQuantities);
        return inventoryServiceInStock.isInStock(skuCodeQuantities);
    }
//    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
//        log.info("Received inventory check request for skuCode: {}", skuCode);
//        return inventoryServiceInStock.isInStock(skuCode);
//    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateStock(@RequestBody Map<String, Integer> skuCodeQuantities) {
        log.info("Received inventory update request for skuCodeQuantities: {}", skuCodeQuantities);
        inventoryServiceInStock.updateStock(skuCodeQuantities);
    }
}

