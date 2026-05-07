package com.ordering.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Double itemPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double subtotal;

    // Getters and Setters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public String getItemName() { return itemName; }
    public Double getItemPrice() { return itemPrice; }
    public Integer getQuantity() { return quantity; }
    public Double getSubtotal() { return subtotal; }

    public void setId(Long id) { this.id = id; }
    public void setOrder(Order order) { this.order = order; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setItemPrice(Double itemPrice) { this.itemPrice = itemPrice; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}