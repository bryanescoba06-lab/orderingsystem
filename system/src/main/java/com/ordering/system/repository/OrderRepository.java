package com.ordering.system.repository;

import com.ordering.system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerNameContainingIgnoreCase(String name);
    List<Order> findByStatus(String status);
    List<Order> findByPaymentStatus(String paymentStatus);
}