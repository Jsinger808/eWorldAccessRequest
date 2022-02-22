package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.EmployeeResponse;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmployeeNotFoundException;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;
import com.example.eworldaccessrequest.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service

public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;


    // Save operation
    public Employee saveEmployee(Employee employee) throws EmptyStringException {
        if ("".equalsIgnoreCase(employee.getFullName()) || "".equalsIgnoreCase(employee.getEmail())) {
            throw new EmptyStringException();
        }

        //Uppercases first letter of each word in FullName.
        employee.setEmail(employee.getEmail().toLowerCase());
        employee.setFullName(WordUtils.capitalize(employee.getFullName().toLowerCase()));

        return employeeRepository.save(employee);
    }

    // List all operation
    public List<Employee> fetchEmployeeList() {

//        List<Employee> employees = employeeRepository.findAll();
//        for (Employee employee : employees) {
//            for (AccessGroup accessGroup : employee.getAccessGroup()) {
//                accessGroup.setExpiration(getExpirationDateByEmployeeID(accessGroup.getEmployeeAccessGroups(), employee.getID()));
//            }
//        }
        return (List<Employee>) employeeRepository.findAll();
    }

//    private LocalDateTime getExpirationDateByEmployeeID(Set<EmployeeAccessGroup> employeeAccessGroups, Long employeeID) {
//        for (EmployeeAccessGroup employeeAccessGroup : employeeAccessGroups) {
//            if (employeeAccessGroup.getEmployeeID().equals(employeeID)) {
//                return employeeAccessGroup.getExpiration();
//            }
//        }
//        return null;
//    }

    // List specific employee operation
    public Employee findByEmail(String email) throws EmployeeNotFoundException {
        if (email == null) {
            throw new EmployeeNotFoundException();
        }
        return employeeRepository.findByEmail(email);
    }

    // Update operation
    public Employee updateEmployee(Employee employee, Long ID) {
        Employee depDB = employeeRepository.findById(ID).get();

        if (Objects.nonNull(depDB.getFullName()) && !"".equalsIgnoreCase(depDB.getFullName())) {
            depDB.setFullName(depDB.getFullName());
        }

        if (Objects.nonNull(depDB.getEmail()) && !"".equalsIgnoreCase(depDB.getEmail())) {
            depDB.setEmail(depDB.getEmail().toLowerCase());
        }

        depDB.setUsResident(employee.isUsResident());

        depDB.setFullTime(employee.isFullTime());

        return employeeRepository.save(depDB);
    }

    // Delete operation
    public void deleteEmployeeById(Long ID) {
        employeeRepository.deleteById(ID);
    }

}
