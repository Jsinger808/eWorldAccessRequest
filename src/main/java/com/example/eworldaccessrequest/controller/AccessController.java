package com.example.eworldaccessrequest.controller;

import com.example.eworldaccessrequest.dto.EmployeeResponse;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroupResponse;
import com.example.eworldaccessrequest.service.EmployeeService;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.service.AccessGroupService;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.service.EmployeeAccessGroupService;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class AccessController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AccessGroupService accessGroupService;
    @Autowired
    private EmployeeAccessGroupService employeeAccessGroupService;

//EMPLOYEE Table

    // Save operation
    @PostMapping("/access/employee")
    public Employee saveEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    // Read all operation
    @GetMapping("/access/employee")
    public List<Employee> fetchEmployeeList() {
        return employeeService.fetchEmployeeList();
    }

    //Read specific employee operation
    @GetMapping("/access/employee/{email}")
    public Employee findByEmail(@PathVariable("email") String email) {
        return employeeService.findByEmail(email);
    }
    
    // Update operation
    @PutMapping("/access/employee/{id}")
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long ID) {
        return employeeService.updateEmployee(employee, ID);
    }

    // Delete operation
    @DeleteMapping("/access/employee/{id}")
    public String deleteEmployeeById(@PathVariable("id") Long ID) {
        employeeService.deleteEmployeeById(ID);
        return "Deleted Successfully";
    }

//ACCESS_GROUP Table

    @PostMapping("/access/access_group")
    public AccessGroup saveAccessGroup(@Valid @RequestBody AccessGroup accessGroup) {
        return accessGroupService.saveAccessGroup(accessGroup);
    }

    // Read operation
    @GetMapping("/access/access_group")
    public List<AccessGroup> fetchAccessGroupList() {
        return accessGroupService.fetchAccessGroupList();
    }

    // Update operation
    @PutMapping("/access/access_group/{id}")
    public AccessGroup updateAccessGroup(@RequestBody AccessGroup accessGroup, @PathVariable("id") Long ID) {
        return accessGroupService.updateAccessGroup(accessGroup, ID);
    }

    // Delete operation
    @DeleteMapping("/access/access_group/{id}")
    public String deleteAccessGroupById(@PathVariable("id") Long ID) {
        accessGroupService.deleteAccessGroupById(ID);
        return "Deleted Successfully";
    }

//EMPLOYEE_ACCESS_GROUP Table

    @PostMapping("/access/employee_access_group")
    @ResponseBody
    public ResponseEntity<?> saveEmployeeAccessGroup(@Valid @RequestBody EmployeeAccessGroup employeeAccessGroup) {
        return new ResponseEntity<EmployeeAccessGroup>(employeeAccessGroup, HttpStatus.OK);
    }

    // Read all operation
//    @GetMapping("/access/employee_access_group")
//    public List<EmployeeAccessGroup> fetchEmployeeAccessGroupList() {
//        return employeeAccessGroupService.fetchEmployeeAccessGroupList();
//    }

    //Read specific employee operation
//    @GetMapping("/access/employee_access_group/{email}")
//    public EmployeeAccessGroupResponse findByEmail(@PathVariable("email") String email) {
//        return employeeAccessGroupService.findByEmail(email);
//    }

    // Update operation
    @PutMapping("/access/employee_access_group/{id}")
    public ResponseEntity<?> updateEmployeeAccessGroup(@RequestBody EmployeeAccessGroup employeeAccessGroup, @PathVariable("id") Long ID) {
        return new ResponseEntity<EmployeeAccessGroup>(employeeAccessGroup, HttpStatus.OK);
    }
//
//    // Delete operation
    @DeleteMapping("/access/employee_access_group/{id}")
    public String deleteEmployeeAccessGroupById(@PathVariable("id") Long ID) {
        employeeAccessGroupService.deleteEmployeeAccessGroupById(ID);
        return "Deleted Successfully";
    }
}
