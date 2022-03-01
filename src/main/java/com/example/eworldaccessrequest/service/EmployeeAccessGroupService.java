package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeAccessGroupDTO;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static EmployeeAccessGroupDTO convertToDto(EmployeeAccessGroup employeeAccessGroup) {

        EmployeeAccessGroupDTO employeeAccessGroupDTO = new EmployeeAccessGroupDTO();
        employeeAccessGroupDTO.setExpiration(employeeAccessGroup.getExpiration());
        employeeAccessGroupDTO.setAccessGroupDTO(AccessGroupService.convertToDto(employeeAccessGroup.getAccessGroup()));

        return employeeAccessGroupDTO;

    }

    // Save operation
    public EmployeeAccessGroup saveEmployeeAccessGroup(EmployeeAccessGroup employeeAccessGroup) throws EmptyStringException {
        if (employeeAccessGroup.getEmployee() == null || employeeAccessGroup.getAccessGroup() == null) {
            throw new EmptyStringException();
        }
        if (!employeeAccessGroup.getAccessGroup().getType().equals("DHS_FORM")) {
            employeeAccessGroup.setExpiration(null);
        }
        return employeeAccessGroupRepository.save(employeeAccessGroup);
    }

    // Read operation
    public List<EmployeeAccessGroupDTO> fetchEmployeeAccessGroupList() {

        List<EmployeeAccessGroup> employeeAccessGroups = employeeAccessGroupRepository.findAll();
        List<EmployeeAccessGroupDTO> employeeAccessGroupDTOs = new ArrayList();

        for (EmployeeAccessGroup employeeAccessGroup : employeeAccessGroups) {
            employeeAccessGroupDTOs.add(convertToDto(employeeAccessGroup));
        }

        return employeeAccessGroupDTOs;
    }

//    public EmployeeAccessGroupResponse findByEmail(String email) {
//        return employeeAccessGroupRepository.findByEmail(email);
//    }

    // Update operation
    public EmployeeAccessGroup updateEmployeeAccessGroup(EmployeeAccessGroup employeeAccessGroup, Long ID) throws DataIntegrityViolationException {
        EmployeeAccessGroup depDB = employeeAccessGroupRepository.findById(ID).get();

        if (Objects.nonNull(employeeAccessGroup.getEmployee().getID()) && employeeAccessGroup.getEmployee().getID() == (Long) employeeAccessGroup.getEmployee().getID()) {
            depDB.getEmployee().setID(employeeAccessGroup.getEmployee().getID());
        }

        if (Objects.nonNull(employeeAccessGroup.getAccessGroup().getID()) && employeeAccessGroup.getAccessGroup().getID() == (Long) employeeAccessGroup.getAccessGroup().getID()) {
            depDB.getAccessGroup().setID(employeeAccessGroup.getAccessGroup().getID());
        }

        if (employeeAccessGroup.getExpiration() == (LocalDateTime) employeeAccessGroup.getExpiration()) {
            depDB.setExpiration(employeeAccessGroup.getExpiration());
        }
        if (!employeeAccessGroup.getAccessGroup().getType().equals("DHS_FORM")) {
            employeeAccessGroup.setExpiration(null);
        }

        return employeeAccessGroupRepository.save(depDB);
    }

    // Delete operation
    public void deleteEmployeeAccessGroupById(Long ID) {
        employeeAccessGroupRepository.deleteById(ID);
    }

}
