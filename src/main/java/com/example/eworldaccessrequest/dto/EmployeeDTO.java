package com.example.eworldaccessrequest.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.List;

@Data
@Component
public class EmployeeDTO {

    private String fullName;

    private String email;

    private boolean fullTime;

    private boolean usResident;

    private List<EmployeeAccessGroupDTO> employeeAccessGroupDTOs;


}
