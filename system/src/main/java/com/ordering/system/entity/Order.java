package com.ordering.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED

    @Column(nullable = false)
    private String paymentMethod; // Cash, Card, E-Wallet

    @Column(nullable = false)
    private String paymentStatus; // Unpaid, Paid

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private Double discount;

    @Column(nullable = false)
    private Double finalAmount;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order",
               cascade = CascadeType.ALL,
               fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    // Getters and Setters
    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public Double getTotalAmount() { return totalAmount; }
    public Double getDiscount() { return discount; }
    public Double getFinalAmount() { return finalAmount; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<OrderItem> getOrderItems() { return orderItems; }

    public void setId(Long id) { this.id = id; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setStatus(String status) { this.status = status; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public void setFinalAmount(Double finalAmount) { this.finalAmount = finalAmount; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}