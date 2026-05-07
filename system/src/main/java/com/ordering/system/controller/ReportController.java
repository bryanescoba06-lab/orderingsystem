package com.ordering.system.controller;

import com.ordering.system.entity.Order;
import com.ordering.system.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final OrderService orderService;

    public ReportController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Reports page
    @GetMapping("/reports")
    public String reportsPage() {
        return "reports";
    }

    // Receipt page for single order
    @GetMapping("/receipt/{id}")
    public String receiptPage(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "receipt";
    }

    // REST - Get sales summary
    @GetMapping("/api/reports/summary")
    @ResponseBody
    public java.util.Map<String, Object> getSummary() {
        List<Order> all = orderService.getAllOrders();

        double totalSales = all.stream()
                .filter(o -> o.getStatus().equals("COMPLETED"))
                .mapToDouble(Order::getFinalAmount).sum();

        long totalOrders   = all.size();
        long completed     = all.stream()
                .filter(o -> o.getStatus().equals("COMPLETED")).count();
        long pending       = all.stream()
                .filter(o -> o.getStatus().equals("PENDING")).count();
        long cancelled     = all.stream()
                .filter(o -> o.getStatus().equals("CANCELLED")).count();
        long unpaid        = all.stream()
                .filter(o -> o.getPaymentStatus().equals("Unpaid")).count();

        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalSales", totalSales);
        summary.put("totalOrders", totalOrders);
        summary.put("completed", completed);
        summary.put("pending", pending);
        summary.put("cancelled", cancelled);
        summary.put("unpaid", unpaid);
        return summary;
    }

    // REST - Get orders by date range
    @GetMapping("/api/reports/orders")
    @ResponseBody
    public List<Order> getOrdersByDate(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        List<Order> all = orderService.getAllOrders();
        if (from != null && to != null) {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate   = LocalDate.parse(to);
            all = all.stream().filter(o -> {
                LocalDate orderDate = o.getOrderDate().toLocalDate();
                return !orderDate.isBefore(fromDate) &&
                       !orderDate.isAfter(toDate);
            }).collect(Collectors.toList());
        }
        return all;
    }
}