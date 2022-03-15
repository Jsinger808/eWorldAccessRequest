package com.example.eworldaccessrequest.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Data
@Component
public class EmployeeDTO {

    private Long ID;

    private String fullName;

    private String email;

    private boolean bes;

    private boolean offshore;

    private List<EmployeeAccessGroupDTO> employeeAccessGroupDTOs;


}
