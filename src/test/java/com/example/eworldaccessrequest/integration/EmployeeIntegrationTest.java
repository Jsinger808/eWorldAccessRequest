//package com.example.eworldaccessrequest.controller;
//
//import com.example.eworldaccessrequest.entity.Employee;
//import com.example.eworldaccessrequest.service.EmployeeService;
//
//import java.util.List;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//
//public class AccessController {
//
//    @Autowired
//    private EmployeeService employeeService;
//
//
//
////EMPLOYEE Table
//
//    // Save operation
//    @PostMapping("/access/employee")
//    public Employee saveEmployee(@Valid @RequestBody Employee employee) {
//        return employeeService.saveEmployee(employee);
//    }
//
//    // Read all operation
//    @GetMapping("/access/employee")
//    public List<Employee> fetchEmployeeList() {
//        return employeeService.fetchEmployeeList();
//    }
//
//    //Read specific employee operation
//    @GetMapping("/access/employee/{email}")
//    public Employee findByEmail(@PathVariable("email") String email) {
//        return employeeService.findByEmail(email);
//    }
//
//    // Update operation
//    @PutMapping("/access/employee/{id}")
//    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long ID) {
//        return employeeService.updateEmployee(employee, ID);
//    }
//
//    // Delete operation
//    @DeleteMapping("/access/employee/{id}")
//    public String deleteEmployeeById(@PathVariable("id") Long ID) {
//        employeeService.deleteEmployeeById(ID);
//        return "Deleted Successfully";
//    }