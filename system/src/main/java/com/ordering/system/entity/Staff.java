package com.ordering.system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String role; // ADMIN, CASHIER, STAFF

    @Column(nullable = false)
    private String shift; // Morning, Afternoon, Evening

    @Column(nullable = false)
    private String status; // Active, Inactive

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private LocalDate hireDate;

    // Getters and Setters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getShift() { return shift; }
    public String getStatus() { return status; }
    public Double getSalary() { return salary; }
    public LocalDate getHireDate() { return hireDate; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(String role) { this.role = role; }
    public void setShift(String shift) { this.shift = shift; }
    public void setStatus(String status) { this.status = status; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
}