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

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/access/")
@CrossOrigin(origins = "http:://localhost:63342")
public class AccessController {


    private final EmployeeService employeeService;
    private final AccessGroupService accessGroupService;
    private final EmployeeAccessGroupService employeeAccessGroupService;

    public AccessController(EmployeeService employeeService,
                            AccessGroupService accessGroupService,
                            EmployeeAccessGroupService employeeAccessGroupService) {
        this.employeeService = employeeService;
        this.accessGroupService = accessGroupService;
        this.employeeAccessGroupService = employeeAccessGroupService;
    }

//EMPLOYEE Table

    // Save operation
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.saveEmployee(employeeDTO);
    }

    // Read operation
    @GetMapping("/employee")
    public List<EmployeeDTO> fetchEmployeeList() {
        return employeeService.fetchEmployeeList();
    }

    //Read specific employee operation
    @GetMapping("/employee/{email}")
    public EmployeeDTO fetchEmployeeByEmail(@PathVariable("email") String email) {
        return employeeService.fetchEmployeeByEmail(email);
    }

    // Find All Employees With Specific Access Group
    @GetMapping("/employee/accessGroup/{accessGroupID}")
    public List<EmployeeDTO> fetchEmployeesByAccessGroupID(@PathVariable("accessGroupID") Long accessGroupID) {
        return employeeService.fetchEmployeesByAccessGroupID(accessGroupID);
    }

    // Find All Employees with Expired Access Groups
    @GetMapping("/employee/expired")
    public List<EmployeeDTO> fetchEmployeesWithExpiredDHSForms() {
        return employeeService.fetchEmployeesWithExpiredDHSForms();
    }

    // Find All Employees with Soon-to-Be Expired Access Groups
    @GetMapping("/employee/soon-expired")
    public List<EmployeeDTO> fetchEmployeesWithSoonToBeExpiredDHSFormsInOneMonth() {
        return employeeService.fetchEmployeesWithSoonToBeExpiredDHSFormsInOneMonth();
    }
    
    // Update operation
    @PutMapping("/employee/{id}")
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable("id") Long ID) {
        return employeeService.updateEmployee(employeeDTO, ID);
    }

    // Delete operation
    @DeleteMapping("/employee/{id}")
    public String deleteEmployeeById(@PathVariable("id") Long ID) {
        employeeService.deleteEmployeeById(ID);
        return "Deleted Successfully";
    }

//ACCESS_GROUP Table

    // Save operation
    @PostMapping("/access_group")
    public AccessGroupDTO saveAccessGroup(@Valid @RequestBody AccessGroup accessGroup) {
        return accessGroupService.saveAccessGroup(accessGroup);
    }

    // Read operation
    @GetMapping("/access_group")
    public List<AccessGroupDTO> fetchAccessGroupList() {
        return accessGroupService.fetchAccessGroupList();
    }

    // Update operation
    @PutMapping("/access_group/{id}")
    public AccessGroupDTO updateAccessGroup(@RequestBody AccessGroup accessGroup, @PathVariable("id") Long ID) {
        return accessGroupService.updateAccessGroup(accessGroup, ID);
    }

    // Delete operation
    @DeleteMapping("/access_group/{id}")
    public String deleteAccessGroupById(@PathVariable("id") Long ID) {
        accessGroupService.deleteAccessGroupById(ID);
        return "Deleted Successfully";
    }

//EMPLOYEE_ACCESS_GROUP Table

    // Save operation
    @Deprecated
    @PostMapping("/employee_access_group")
    public EmployeeAccessGroupDTO saveEmployeeAccessGroup(@Valid @RequestBody EmployeeAccessGroup employeeAccessGroup) {
        return employeeAccessGroupService.saveEmployeeAccessGroup(employeeAccessGroup);
    }

    // Read operation
    @Deprecated
    @GetMapping("/employee_access_group")
    public List<EmployeeAccessGroupDTO> fetchEmployeeAccessGroupList() {
        return employeeAccessGroupService.fetchEmployeeAccessGroupList();
    }

    // Update operation
    @Deprecated
    @PutMapping("/employee_access_group/{id}")
    public EmployeeAccessGroupDTO updateEmployeeAccessGroup(@RequestBody EmployeeAccessGroup employeeAccessGroup, @PathVariable("id") Long ID) {
        return employeeAccessGroupService.updateEmployeeAccessGroup(employeeAccessGroup, ID);
    }

    // Delete operation
    @Deprecated
    @DeleteMapping("/employee_access_group/{id}")
    public String deleteEmployeeAccessGroupById(@PathVariable("id") Long ID) {
        employeeAccessGroupService.deleteEmployeeAccessGroupById(ID);
        return "Deleted Successfully";
    }
}
