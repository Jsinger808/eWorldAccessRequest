package com.example.eworldaccessrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long ID;

    private String fullName;

    private String email;

    private boolean bes;

    private boolean offshore;

    private List<Long> accessGroupIDs;

    private List<EmployeeAccessGroupDTO> employeeAccessGroupDTOs;


}
