package com.example.eworldaccessrequest.controller;

import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeAccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.service.EmployeeService;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.service.AccessGroupService;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.service.EmployeeAccessGroupService;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
    public EmployeeDTO saveEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    // Read operation
    @GetMapping("/access/employee")
    public List<EmployeeDTO> fetchEmployeeList() {
        return employeeService.fetchEmployeeList();
    }

    //Read specific employee operation
    @GetMapping("/access/employee/{email}")
    public EmployeeDTO findByEmail(@PathVariable("email") String email) {
        return employeeService.findByEmail(email);
    }
    
    // Update operation
    @PutMapping("/access/employee/{id}")
    public EmployeeDTO updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long ID) {
        return employeeService.updateEmployee(employee, ID);
    }

    // Delete operation
    @DeleteMapping("/access/employee/{id}")
    public String deleteEmployeeById(@PathVariable("id") Long ID) {
        employeeService.deleteEmployeeById(ID);
        return "Deleted Successfully";
    }

//ACCESS_GROUP Table

    // Save operation
    @PostMapping("/access/access_group")
    public AccessGroupDTO saveAccessGroup(@Valid @RequestBody AccessGroup accessGroup) {
        return accessGroupService.saveAccessGroup(accessGroup);
    }

    // Read operation
    @GetMapping("/access/access_group")
    public List<AccessGroupDTO> fetchAccessGroupList() {
        return accessGroupService.fetchAccessGroupList();
    }

    // Update operation
    @PutMapping("/access/access_group/{id}")
    public AccessGroupDTO updateAccessGroup(@RequestBody AccessGroup accessGroup, @PathVariable("id") Long ID) {
        return accessGroupService.updateAccessGroup(accessGroup, ID);
    }

    // Delete operation
    @DeleteMapping("/access/access_group/{id}")
    public String deleteAccessGroupById(@PathVariable("id") Long ID) {
        accessGroupService.deleteAccessGroupById(ID);
        return "Deleted Successfully";
    }

//EMPLOYEE_ACCESS_GROUP Table

    // Save operation
    @PostMapping("/access/employee_access_group")
    public EmployeeAccessGroupDTO saveEmployeeAccessGroup(@Valid @RequestBody EmployeeAccessGroup employeeAccessGroup) {
        return employeeAccessGroupService.saveEmployeeAccessGroup(employeeAccessGroup);
    }

    // Read operation
    @GetMapping("/access/employee_access_group")
    public List<EmployeeAccessGroupDTO> fetchEmployeeAccessGroupList() {
        return employeeAccessGroupService.fetchEmployeeAccessGroupList();
    }


    // Update operation
    @PutMapping("/access/employee_access_group/{id}")
    public EmployeeAccessGroupDTO updateEmployeeAccessGroup(@RequestBody EmployeeAccessGroup employeeAccessGroup, @PathVariable("id") Long ID) {
        return employeeAccessGroupService.updateEmployeeAccessGroup(employeeAccessGroup, ID);
    }

    // Delete operation
    @DeleteMapping("/access/employee_access_group/{id}")
    public String deleteEmployeeAccessGroupById(@PathVariable("id") Long ID) {
        employeeAccessGroupService.deleteEmployeeAccessGroupById(ID);
        return "Deleted Successfully";
    }
}
