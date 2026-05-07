package com.ordering.system.controller;

import com.ordering.system.entity.Staff;
import com.ordering.system.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LaborController {

    private final StaffService staffService;

    public LaborController(StaffService staffService) {
        this.staffService = staffService;
    }

    // Thymeleaf page
    @GetMapping("/labor-management")
    public String laborManagementPage() {
        return "labor-management";
    }

    // REST - Get all staff
    @GetMapping("/api/staff")
    @ResponseBody
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    // REST - Get staff by ID
    @GetMapping("/api/staff/{id}")
    @ResponseBody
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    // REST - Add staff
    @PostMapping("/api/staff")
    @ResponseBody
    public ResponseEntity<Staff> addStaff(@RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.addStaff(staff));
    }

    // REST - Update staff
    @PutMapping("/api/staff/{id}")
    @ResponseBody
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id,
                                              @RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.updateStaff(id, staff));
    }

    // REST - Delete staff
    @DeleteMapping("/api/staff/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    // REST - Search staff
    @GetMapping("/api/staff/search")
    @ResponseBody
    public ResponseEntity<List<Staff>> searchStaff(@RequestParam String name) {
        return ResponseEntity.ok(staffService.searchStaff(name));
    }
}