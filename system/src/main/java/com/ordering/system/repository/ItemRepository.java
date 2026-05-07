package com.ordering.system.repository;

import com.ordering.system.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String name);
    Optional<Item> findByNameIgnoreCase(String name); // ← must be here
}