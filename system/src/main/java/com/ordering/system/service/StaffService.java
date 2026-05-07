package com.ordering.system.service;

import com.ordering.system.entity.Staff;
import com.ordering.system.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
    }

    public Staff addStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public Staff updateStaff(Long id, Staff updated) {
        Staff existing = getStaffById(id);
        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setRole(updated.getRole());
        existing.setShift(updated.getShift());
        existing.setStatus(updated.getStatus());
        existing.setSalary(updated.getSalary());
        existing.setHireDate(updated.getHireDate());
        return staffRepository.save(existing);
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    public List<Staff> searchStaff(String name) {
        return staffRepository.findByFullNameContainingIgnoreCase(name);
    }

    public List<Staff> filterByRole(String role) {
        return staffRepository.findByRole(role);
    }

    public List<Staff> filterByStatus(String status) {
        return staffRepository.findByStatus(status);
    }
}