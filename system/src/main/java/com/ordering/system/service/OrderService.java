package com.ordering.system.service;

import com.ordering.system.entity.Order;
import com.ordering.system.entity.OrderItem;
import com.ordering.system.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Order createOrder(Order order) {
        // Generate unique order number
        order.setOrderNumber("ORD-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentStatus("Unpaid");

        // Link each order item back to the order
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
                item.setSubtotal(item.getItemPrice() * item.getQuantity());
            }
        }

        // Compute totals
        double total = order.getOrderItems() == null ? 0 :
                order.getOrderItems().stream()
                        .mapToDouble(OrderItem::getSubtotal).sum();
        order.setTotalAmount(total);

        double discount = order.getDiscount() != null ? order.getDiscount() : 0;
        order.setDiscount(discount);
        order.setFinalAmount(total - discount);

        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order updatePaymentStatus(Long id, String paymentStatus) {
        Order order = getOrderById(id);
        order.setPaymentStatus(paymentStatus);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> searchByCustomer(String name) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(name);
    }

    public List<Order> filterByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}