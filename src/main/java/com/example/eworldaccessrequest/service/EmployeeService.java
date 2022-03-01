package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.EmployeeAccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmployeeNotFoundException;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.text.WordUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeDTO convertToDto(Employee employee) {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFullName(employee.getFullName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setFullTime(employee.isFullTime());
        employeeDTO.setUsResident(employee.isUsResident());
        List<EmployeeAccessGroupDTO> employeeAccessGroupDTOs = new ArrayList();
        for (EmployeeAccessGroup employeeAccessGroup : employee.getEmployeeAccessGroups()) {
            employeeAccessGroupDTOs.add(EmployeeAccessGroupService.convertToDto(employeeAccessGroup));
        }
        employeeDTO.setEmployeeAccessGroupDTOs(employeeAccessGroupDTOs);

        return employeeDTO;

    }

    // Save operation
    public EmployeeDTO saveEmployee(Employee employee) throws EmptyStringException {
        if ("".equalsIgnoreCase(employee.getFullName()) || "".equalsIgnoreCase(employee.getEmail())) {
            throw new EmptyStringException();
        }

        //Uppercases first letter of each word in FullName.
        employee.setEmail(employee.getEmail().toLowerCase());
        employee.setFullName(WordUtils.capitalize(employee.getFullName().toLowerCase()));

        return convertToDto(employeeRepository.save(employee));
    }

    // List all operation
    public List<EmployeeDTO> fetchEmployeeList() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOs = new ArrayList();
        for (Employee employee : employees) {
            employeeDTOs.add(convertToDto(employee));
        }
        return employeeDTOs;
    }

    // List specific employee operation
    public EmployeeDTO findByEmail(String email) throws EmployeeNotFoundException{
        if (email == null || employeeRepository.findByEmail(email) == null) {
            throw new EmployeeNotFoundException();
        }
        return convertToDto(employeeRepository.findByEmail(email));
    }

    // Update operation
    public EmployeeDTO updateEmployee(Employee employee, Long ID) {
        Employee depDB = employeeRepository.findById(ID).get();

        if (Objects.nonNull(employee.getFullName()) && !"".equalsIgnoreCase(employee.getFullName())) {
            depDB.setFullName(WordUtils.capitalize(employee.getFullName().toLowerCase()));
        }

        if (Objects.nonNull(employee.getEmail()) && !"".equalsIgnoreCase(employee.getEmail())) {
            depDB.setEmail(employee.getEmail().toLowerCase());
        }

        depDB.setUsResident(employee.isUsResident());

        depDB.setFullTime(employee.isFullTime());

        return convertToDto(employeeRepository.save(depDB));
    }

    // Delete operation
    public void deleteEmployeeById(Long ID) {
        employeeRepository.deleteById(ID);
    }

}
