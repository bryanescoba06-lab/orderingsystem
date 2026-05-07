package com.ordering.system.controller;

import com.ordering.system.entity.Order;
import com.ordering.system.entity.Item;
import com.ordering.system.entity.Staff;
import com.ordering.system.repository.OrderRepository;
import com.ordering.system.repository.ItemRepository;
import com.ordering.system.repository.StaffRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final StaffRepository staffRepository;

    public DashboardController(OrderRepository orderRepository,
                               ItemRepository itemRepository,
                               StaffRepository staffRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository  = itemRepository;
        this.staffRepository = staffRepository;
    }

    // Summary cards
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        List<Order> orders = orderRepository.findAll();
        List<Item>  items  = itemRepository.findAll();
        List<Staff> staff  = staffRepository.findAll();

        double totalSales = orders.stream()
                .filter(o -> o.getStatus().equals("COMPLETED"))
                .mapToDouble(Order::getFinalAmount).sum();

        long totalOrders    = orders.size();
        long completedOrders = orders.stream()
                .filter(o -> o.getStatus().equals("COMPLETED")).count();
        long pendingOrders  = orders.stream()
                .filter(o -> o.getStatus().equals("PENDING")).count();
        long totalItems     = items.size();
        long lowStockItems  = items.stream()
                .filter(i -> i.getQuantity() <= 5).count();
        long totalStaff     = staff.size();
        long activeStaff    = staff.stream()
                .filter(s -> s.getStatus().equals("Active")).count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSales",      totalSales);
        summary.put("totalOrders",     totalOrders);
        summary.put("completedOrders", completedOrders);
        summary.put("pendingOrders",   pendingOrders);
        summary.put("totalItems",      totalItems);
        summary.put("lowStockItems",   lowStockItems);
        summary.put("totalStaff",      totalStaff);
        summary.put("activeStaff",     activeStaff);
        return summary;
    }

    // Daily sales - last 7 days
    @GetMapping("/sales/daily")
    public Map<String, Object> getDailySales() {
        List<Order> orders = orderRepository.findAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd");

        Map<String, Double> salesMap = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String label = date.format(fmt);
            double total = orders.stream()
                    .filter(o -> o.getStatus().equals("COMPLETED"))
                    .filter(o -> o.getOrderDate().toLocalDate().equals(date))
                    .mapToDouble(Order::getFinalAmount).sum();
            salesMap.put(label, total);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(salesMap.keySet()));
        result.put("data",   new ArrayList<>(salesMap.values()));
        return result;
    }

    // Monthly sales - last 6 months
    @GetMapping("/sales/monthly")
    public Map<String, Object> getMonthlySales() {
        List<Order> orders = orderRepository.findAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy");

        Map<String, Double> salesMap = new LinkedHashMap<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate date  = LocalDate.now().minusMonths(i);
            String label    = date.format(fmt);
            int month       = date.getMonthValue();
            int year        = date.getYear();
            double total    = orders.stream()
                    .filter(o -> o.getStatus().equals("COMPLETED"))
                    .filter(o -> o.getOrderDate().getMonthValue() == month
                              && o.getOrderDate().getYear() == year)
                    .mapToDouble(Order::getFinalAmount).sum();
            salesMap.put(label, total);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(salesMap.keySet()));
        result.put("data",   new ArrayList<>(salesMap.values()));
        return result;
    }

    // Top 5 best selling items
    @GetMapping("/top-items")
    public List<Map<String, Object>> getTopItems() {
        List<Order> orders = orderRepository.findAll();

        Map<String, Integer> itemCount = new HashMap<>();
        orders.stream()
                .filter(o -> o.getStatus().equals("COMPLETED"))
                .forEach(o -> {
                    if (o.getOrderItems() != null) {
                        o.getOrderItems().forEach(item -> {
                            itemCount.merge(
                                item.getItemName(),
                                item.getQuantity(), Integer::sum);
                        });
                    }
                });

        return itemCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .limit(5)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name",     e.getKey());
                    map.put("quantity", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // Recent 5 orders
    @GetMapping("/recent-orders")
    public List<Map<String, Object>> getRecentOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(5)
                .map(o -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderNumber",   o.getOrderNumber());
                    map.put("customerName",  o.getCustomerName());
                    map.put("finalAmount",   o.getFinalAmount());
                    map.put("status",        o.getStatus());
                    map.put("paymentStatus", o.getPaymentStatus());
                    map.put("orderDate",     o.getOrderDate().toString());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // Order status breakdown
    @GetMapping("/order-status")
    public Map<String, Object> getOrderStatus() {
        List<Order> orders = orderRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("labels", List.of(
            "Completed", "Pending", "Cancelled", "Confirmed"));
        result.put("data", List.of(
            orders.stream().filter(o ->
                o.getStatus().equals("COMPLETED")).count(),
            orders.stream().filter(o ->
                o.getStatus().equals("PENDING")).count(),
            orders.stream().filter(o ->
                o.getStatus().equals("CANCELLED")).count(),
            orders.stream().filter(o ->
                o.getStatus().equals("CONFIRMED")).count()
        ));
        return result;
    }
}