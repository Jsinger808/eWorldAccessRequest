package com.example.eworldaccessrequest.controller;

import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.dto.AppUserDTO;
import com.example.eworldaccessrequest.entity.AppUser;
import com.example.eworldaccessrequest.entity.Role;
import com.example.eworldaccessrequest.service.EmployeeService;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.service.AccessGroupService;
import com.example.eworldaccessrequest.service.AppUserService;


import java.net.URI;
import java.util.List;
import javax.validation.Valid;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/access/")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*")
public class AccessController {

    private final EmployeeService employeeService;
    private final AccessGroupService accessGroupService;
    private final AppUserService appUserService;

    public AccessController(EmployeeService employeeService,
                            AccessGroupService accessGroupService, AppUserService appUserService) {
        this.employeeService = employeeService;
        this.accessGroupService = accessGroupService;
        this.appUserService = appUserService;
    }

//USER Table

    @PostMapping("/user")
    public ResponseEntity<AppUserDTO> saveUser(@Valid @RequestBody AppUserDTO appUserDTO) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/access/user").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveUser(appUserDTO));
    }

    @PostMapping("/role")
    public ResponseEntity<Role> saveRole(@Valid @RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/access/role").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveRole(role));
    }

    @PostMapping("/role/user")
    public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        appUserService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @Data
    class RoleToUserForm {
        private String username;
        private String roleName;
    }

//    @GetMapping("/user")
//    public void checkUserPassword(@Valid @RequestBody AppUserDTO appUserDTO) throws Exception {
//        appUserService.checkUserPassword(appUserDTO);
//    }

    @GetMapping("/user/{username}")
    public void fetchUserByUsername(@PathVariable("username") String username) {
        appUserService.fetchUserByUsername(username);
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

    //Read specific employee operation by ID
    @GetMapping("/employee/id/{employeeID}")
    public EmployeeDTO fetchEmployeeByID(@PathVariable("employeeID") Long employeeID) {
        return employeeService.fetchEmployeeByID(employeeID);
    }

    //Read specific employee operation by email
    @GetMapping("/employee/{email}")
    public EmployeeDTO fetchEmployeeByEmail(@PathVariable("email") String email) {
        return employeeService.fetchEmployeeByEmail(email);
    }

    // Find All Employees With Specific Access Group
    @GetMapping("/employee/accessGroup/{accessGroupID}")
    public List<EmployeeDTO> fetchEmployeesByAccessGroupID(@PathVariable("accessGroupID") Long accessGroupID) {
        return employeeService.fetchEmployeesByAccessGroupID(accessGroupID);
    }

/*    // Find All Employees with Expired Access Groups
    @Deprecated
    @GetMapping("/employee/expired")
    public List<EmployeeDTO> fetchEmployeesWithExpiredDHSForms() {
        return employeeService.fetchEmployeesWithExpiredDHSForms();
    }

    // Find All Employees with Soon-to-Be Expired Access Groups
    @Deprecated
    @GetMapping("/employee/soon-expired")
    public List<EmployeeDTO> fetchEmployeesWithSoonToBeExpiredDHSFormsInOneMonth() {
        return employeeService.fetchEmployeesWithSoonToBeExpiredDHSFormsInOneMonth();
    }*/

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
}
