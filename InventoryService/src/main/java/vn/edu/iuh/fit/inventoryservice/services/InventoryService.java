package vn.edu.iuh.fit.inventoryservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.inventoryservice.models.Inventory;
import vn.edu.iuh.fit.inventoryservice.repositories.InventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventory) {
        Optional<Inventory> existingInventoryOptional = inventoryRepository.findById(id);
        if (existingInventoryOptional.isPresent()) {
            inventory.setId(id);
            return inventoryRepository.save(inventory);
        } else {
            return null; // or throw exception
        }
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
