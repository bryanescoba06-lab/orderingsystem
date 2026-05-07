package com.ordering.system.service;

import com.ordering.system.entity.Item;
import com.ordering.system.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Get all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Get item by ID
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    // Add new item
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    // Update item
    public Item updateItem(Long id, Item updatedItem) {
        Item existing = getItemById(id);
        existing.setName(updatedItem.getName());
        existing.setCategory(updatedItem.getCategory());
        existing.setPrice(updatedItem.getPrice());
        existing.setQuantity(updatedItem.getQuantity());
        existing.setDescription(updatedItem.getDescription());
        return itemRepository.save(existing);
    }

    // Delete item
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // Search item by name
    public List<Item> searchItems(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }
 // Deduct stock quantity
    public void deductStock(Long itemId, int quantity) {
        Item item = getItemById(itemId);

        if (item.getQuantity() < quantity) {
            throw new RuntimeException(
                "Insufficient stock for item: " + item.getName() +
                ". Available: " + item.getQuantity());
        }

        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);
    }

    // Check if stock is sufficient
    public boolean isStockAvailable(Long itemId, int quantity) {
        Item item = getItemById(itemId);
        return item.getQuantity() >= quantity;
    }
}