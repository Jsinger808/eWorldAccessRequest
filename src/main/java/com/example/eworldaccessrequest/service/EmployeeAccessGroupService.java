package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.EmployeeAccessGroupDTO;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;

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

    // DTO Converter
    public static EmployeeAccessGroupDTO convertToDto(EmployeeAccessGroup employeeAccessGroup) {

        EmployeeAccessGroupDTO employeeAccessGroupDTO = new EmployeeAccessGroupDTO();
        employeeAccessGroupDTO.setExpiration(employeeAccessGroup.getExpiration());
        employeeAccessGroupDTO.setAccessGroupDTO(AccessGroupService.convertToDto(employeeAccessGroup.getAccessGroup()));

        return employeeAccessGroupDTO;

    }

    // Save
    public EmployeeAccessGroupDTO saveEmployeeAccessGroup(EmployeeAccessGroup employeeAccessGroup) throws EmptyStringException {
        if (employeeAccessGroup.getEmployee() == null || "".equalsIgnoreCase(employeeAccessGroup.getEmployee().getFullName())
                || employeeAccessGroup.getAccessGroup() == null || "".equalsIgnoreCase(employeeAccessGroup.getAccessGroup().getName())) {
            throw new EmptyStringException();
        }
        if (!employeeAccessGroup.getAccessGroup().getType().equals("DHS_FORM")) {
            employeeAccessGroup.setExpiration(null);
        }
        return convertToDto(employeeAccessGroupRepository.save(employeeAccessGroup));
    }

    // Read
    public List<EmployeeAccessGroupDTO> fetchEmployeeAccessGroupList() {

        List<EmployeeAccessGroup> employeeAccessGroups = employeeAccessGroupRepository.findAll();
        List<EmployeeAccessGroupDTO> employeeAccessGroupDTOs = new ArrayList();

        for (EmployeeAccessGroup employeeAccessGroup : employeeAccessGroups) {
            employeeAccessGroupDTOs.add(convertToDto(employeeAccessGroup));
        }

        return employeeAccessGroupDTOs;
    }

    // Update
    public EmployeeAccessGroupDTO updateEmployeeAccessGroup(EmployeeAccessGroup employeeAccessGroup, Long ID) throws DataIntegrityViolationException {
        EmployeeAccessGroup depDB = employeeAccessGroupRepository.findById(ID).get();

        if (Objects.nonNull(employeeAccessGroup.getEmployee().getID())) {
            depDB.setEmployee(employeeAccessGroup.getEmployee());

        }

        if (Objects.nonNull(employeeAccessGroup.getAccessGroup().getID())) {
            depDB.setAccessGroup(employeeAccessGroup.getAccessGroup());
        }

        if (!employeeAccessGroup.getAccessGroup().getType().equals("DHS_FORM")) {
           depDB.setExpiration(null);
        }
        else {
            depDB.setExpiration(employeeAccessGroup.getExpiration());
        }

        return convertToDto(employeeAccessGroupRepository.save(depDB));
    }

    // Delete
    public void deleteEmployeeAccessGroupById(Long ID) {
        employeeAccessGroupRepository.deleteById(ID);
    }

}
