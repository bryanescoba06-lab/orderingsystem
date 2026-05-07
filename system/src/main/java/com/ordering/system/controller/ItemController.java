package com.ordering.system.controller;

import com.ordering.system.entity.Item;
import com.ordering.system.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Thymeleaf page
    @GetMapping("/item-management")
    public String itemManagementPage() {
        return "item-management";
    }

    // REST - Get all items
    @GetMapping("/api/items")
    @ResponseBody
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    // REST - Get item by ID
    @GetMapping("/api/items/{id}")
    @ResponseBody
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // REST - Add item
    @PostMapping("/api/items")
    @ResponseBody
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.addItem(item));
    }

    // REST - Update item
    @PutMapping("/api/items/{id}")
    @ResponseBody
    public ResponseEntity<Item> updateItem(@PathVariable Long id,
                                            @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(id, item));
    }

    // REST - Delete item
    @DeleteMapping("/api/items/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // REST - Search items
    @GetMapping("/api/items/search")
    @ResponseBody
    public ResponseEntity<List<Item>> searchItems(@RequestParam String name) {
        return ResponseEntity.ok(itemService.searchItems(name));
    }
}