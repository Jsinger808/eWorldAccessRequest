package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.repository.EmployeeRepository;

// Importing required classes
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Annotation
@Service

public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Save operation
    @Override
    public Employee saveEmployee(Employee employee)
    {
        return employeeRepository.save(employee);
    }

    // Read operation
    @Override public List<Employee> fetchEmployeeList()
    {
        return (List<Employee>)
                employeeRepository.findAll();
    }

    // Update operation
    @Override
    public Employee
    updateEmployee(Employee employee, Long employeeId)
    {
        Employee depDB = employeeRepository.findById(employeeId).get();

        if (Objects.nonNull(depDB.getFullName()) && !"".equalsIgnoreCase(depDB.getFullName())) {
            depDB.setFullName(depDB.getFullName());
        }

        if (Objects.nonNull(depDB.getEmail()) && !"".equalsIgnoreCase(depDB.getEmail())) {
            depDB.setEmail(depDB.getEmail());
        }

        depDB.setUsResident(employee.isUsResident());

        //With lombok, booleans use .is instead of .get
        depDB.setFullTime(employee.isFullTime());

        //Change all table names to nonPlural -- employee instead of employees.
        return employeeRepository.save(depDB);
    }

    // Delete operation
    @Override
    public void deleteEmployeeById(Long employeeId)
    {
        employeeRepository.deleteById(employeeId);
    }

}
