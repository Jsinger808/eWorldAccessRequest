package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.dto.EmployeeExpiredDTO;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.*;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;
import com.example.eworldaccessrequest.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeAccessGroupRepository employeeAccessGroupRepository;

    public EmployeeDTO convertToDto(Employee employee) {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setID(employee.getID());
        employeeDTO.setFullName(employee.getFullName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setBes(employee.isOffshore());
        employeeDTO.setOffshore(employee.isOffshore());
        ArrayList employeeAccessGroupDTOs = new ArrayList();
        if (employee.getEmployeeAccessGroups() != null) {
            for (EmployeeAccessGroup employeeAccessGroup : employee.getEmployeeAccessGroups()) {
                employeeAccessGroupDTOs.add(EmployeeAccessGroupService.convertToDto(employeeAccessGroup));
            }
        }
        employeeDTO.setEmployeeAccessGroupDTOs(employeeAccessGroupDTOs);

        return employeeDTO;

    }

    public EmployeeExpiredDTO convertToExpiredDto(Employee employee) {
        EmployeeExpiredDTO employeeExpiredDTO = new EmployeeExpiredDTO();
        employeeExpiredDTO.setFullName(employee.getFullName());
        return employeeExpiredDTO;
    }

    // Save operation
    public EmployeeDTO saveEmployee(Employee employee) throws EmptyStringException, InvalidEmailException {
        if ("".equalsIgnoreCase(employee.getFullName()) || "".equalsIgnoreCase(employee.getEmail())) {
            throw new EmptyStringException();
        }
        if (!employee.getEmail().contains("@eworldes.com")) {
            throw new InvalidEmailException();
        }

        //Uppercases first letter of each word in FullName.
        employee.setEmail(employee.getEmail().toLowerCase());
        employee.setFullName(WordUtils.capitalize(employee.getFullName().toLowerCase()));

        return convertToDto(employeeRepository.save(employee));
    }

    // List all operation
    public List<EmployeeDTO> fetchEmployeeList() {
        List<Employee> employees = employeeRepository.findAll();
        ArrayList employeeDTOs = new ArrayList();
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

    // List employees by specific access group
    public List<EmployeeDTO> findByAccessGroupID(Long accessGroupID) throws EmployeeNotFoundException {
        if (employeeRepository.findEmployeesByAccessGroupID(accessGroupID).size() == 0){
            throw new EmployeeNotFoundException();
        }
        List<Employee> employees = employeeRepository.findEmployeesByAccessGroupID(accessGroupID);
        ArrayList employeeDTOs = new ArrayList();
        for (Employee employee : employees) {
            employeeDTOs.add(convertToDto(employee));
        }
        return employeeDTOs;
    }

    // List employees with expired DHS_Forms and only shows those access groups
    public List<EmployeeDTO> findEmployeesWithExpiredDHSForms() throws NoEmployeesWithExpiredAccessGroupsException {
        LocalDate rightNow = LocalDate.now();
        if (employeeRepository.findEmployeesWithExpiredDHSForms(rightNow).size() == 0){
            throw new NoEmployeesWithExpiredAccessGroupsException();
        }
        List<Employee> employees = employeeRepository.findEmployeesWithExpiredDHSForms(rightNow).stream()
                .distinct()
                .collect(Collectors.toList());

        ArrayList employeeDTOs = new ArrayList();
        ArrayList invalidEmployeeAccessGroups = new ArrayList();
        for (Employee employee : employees) {
            for (EmployeeAccessGroup employeeAccessGroup : employee.getEmployeeAccessGroups()) {
                if (employeeAccessGroup.getExpiration() == null) {
                    invalidEmployeeAccessGroups.add(employeeAccessGroup);
                }
            }
            employee.getEmployeeAccessGroups().removeAll(invalidEmployeeAccessGroups);
            invalidEmployeeAccessGroups.clear();
            employeeDTOs.add(convertToDto(employee));
        }
        return employeeDTOs;
    }

    // List employees with DHS_Forms that are about to expire
    public List<EmployeeDTO> findEmployeesWithSoonToBeExpiredDHSFormsInOneMonth() throws NoEmployeesWithSoonToBeExpiredAccessGroupsException {
        LocalDate rightNow = LocalDate.now();
        LocalDate oneMonth = rightNow.plusMonths(1);
        if (employeeRepository.findEmployeesWithSoonToBeExpiredDHSFormsInOneMonth(rightNow, oneMonth).size() == 0){
            throw new NoEmployeesWithSoonToBeExpiredAccessGroupsException();
        }
        List<Employee> employees = employeeRepository.findEmployeesWithSoonToBeExpiredDHSFormsInOneMonth(rightNow, oneMonth).stream()
                .distinct()
                .collect(Collectors.toList());

        ArrayList employeeDTOs = new ArrayList();
        ArrayList invalidEmployeeAccessGroups = new ArrayList();
        for (Employee employee : employees) {
            for (EmployeeAccessGroup employeeAccessGroup : employee.getEmployeeAccessGroups()) {
                if (employeeAccessGroup.getExpiration() == null) {
                    invalidEmployeeAccessGroups.add(employeeAccessGroup);
                }
            }
            employee.getEmployeeAccessGroups().removeAll(invalidEmployeeAccessGroups);
            invalidEmployeeAccessGroups.clear();
            employeeDTOs.add(convertToDto(employee));
        }
        return employeeDTOs;
    }

//    public List<EmployeeDTO> findByAccessGroupID(Long accessGroupID) throws EmployeeNotFoundException{
//        if (employeeAccessGroupRepository.findAllByAccessGroup_ID(accessGroupID) == null) {
//            throw new EmployeeNotFoundException();
//        }
//        List<EmployeeAccessGroup> employeeAccessGroups = employeeAccessGroupRepository.findAllByAccessGroup_ID(accessGroupID);
//        List<Employee> employees = new ArrayList<>();
//        for (EmployeeAccessGroup employeeAccessGroup : employeeAccessGroups) {
//            employees.add(employeeRepository.findByEmployeeAccessGroupsContaining(employeeAccessGroup));
//        }
//        ArrayList employeeDTOs = new ArrayList();
//        for (Employee employee : employees) {
//            employeeDTOs.add(convertToDto(employee));
//        }
//        return employeeDTOs;
//    }

    // Update operation
    public EmployeeDTO updateEmployee(Employee employee, Long ID) {
        Employee depDB = employeeRepository.findById(ID).get();

        if (Objects.nonNull(employee.getFullName()) && !"".equalsIgnoreCase(employee.getFullName())) {
            depDB.setFullName(WordUtils.capitalize(employee.getFullName().toLowerCase()));
        }

        if (Objects.nonNull(employee.getEmail()) && !"".equalsIgnoreCase(employee.getEmail()) && employee.getEmail().contains("@eworldes.com")) {
            depDB.setEmail(employee.getEmail().toLowerCase());
        }

        depDB.setOffshore(employee.isOffshore());

        depDB.setBes(employee.isBes());

        return convertToDto(employeeRepository.save(depDB));
    }

    // Delete operation
    public void deleteEmployeeById(Long ID) {
        employeeRepository.deleteById(ID);
    }

}
