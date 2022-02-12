package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.entity.Employee;

// Importing required classes
import java.util.List;

public interface EmployeeService {

    // Save operation
    Employee saveEmployee(Employee employee);

    // Read operation
    List<Employee> fetchEmployeeList();

    // Update operation
    Employee updateEmployee(Employee employee, Long employeeId);

    // Delete operation
    void deleteEmployeeById(Long employeeId);

}
