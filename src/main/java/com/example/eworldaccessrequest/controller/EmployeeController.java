package com.example.eworldaccessrequest.controller;

import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.service.EmployeeService;
import java.util.List;
import javax.validation.Valid;
// Importing required classes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Annotation
@RestController

//Class
public class EmployeeController {

    // Annotation
    @Autowired private EmployeeService employeeService;

    // Save operation
    @PostMapping("/employee")
    public Employee saveEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    // Read operation
    @GetMapping("/employee")
    public List<Employee> fetchEmployeeList() {
        return employeeService.fetchEmployeeList();
    }

    // Update operation
    @PutMapping("/employee/{id}")
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long employeeId) {
        return employeeService.updateEmployee(employee, employeeId);
    }

    // Delete operation
    @DeleteMapping("/employee/{id}")
    public String deleteEmployeeById(@PathVariable("id") Long employeeId) {
        employeeService.deleteEmployeeById(employeeId);
        return "Deleted Successfully";
    }
}
