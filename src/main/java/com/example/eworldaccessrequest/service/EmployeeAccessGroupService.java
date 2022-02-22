package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroupResponse;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;
import com.example.eworldaccessrequest.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

// Annotation
@Service

public class EmployeeAccessGroupService {

    @Autowired
    private EmployeeAccessGroupRepository employeeAccessGroupRepository;

    // Save operation
    public EmployeeAccessGroup saveEmployeeAccessGroup(EmployeeAccessGroup employeeAccessGroup) throws EmptyStringException {
        if (employeeAccessGroup.getEmployeeID() == null || employeeAccessGroup.getAccessGroupID() == null) {
            throw new EmptyStringException();
        }
        return employeeAccessGroupRepository.save(employeeAccessGroup);
    }

    // Read operation
    public List<EmployeeAccessGroup> fetchEmployeeAccessGroupList() {
        return employeeAccessGroupRepository.findAll();
    }

//    public EmployeeAccessGroupResponse findByEmail(String email) {
//        return employeeAccessGroupRepository.findByEmail(email);
//    }

    // Update operation
    public EmployeeAccessGroup updateEmployeeAccessGroup(EmployeeAccessGroup employeeAccessGroup, Long ID) throws DataIntegrityViolationException {
        EmployeeAccessGroup depDB = employeeAccessGroupRepository.findById(ID).get();

        if (Objects.nonNull(depDB.getEmployeeID()) && depDB.getEmployeeID() == (Long) depDB.getEmployeeID()) {
            depDB.setEmployeeID(depDB.getEmployeeID());
        }

        if (Objects.nonNull(depDB.getAccessGroupID()) && depDB.getAccessGroupID() == (Long) depDB.getAccessGroupID()) {
            depDB.setAccessGroupID(depDB.getAccessGroupID());
        }

        if (depDB.getExpiration() == (LocalDateTime) depDB.getExpiration()) {
            depDB.setExpiration(depDB.getExpiration());
        }

        return employeeAccessGroupRepository.save(depDB);
    }

    // Delete operation
    public void deleteEmployeeAccessGroupById(Long ID) {
        employeeAccessGroupRepository.deleteById(ID);
    }

}
