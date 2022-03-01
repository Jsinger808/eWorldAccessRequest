package com.example.eworldaccessrequest.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;



@Data
@Component
public class EmployeeAccessGroupDTO {

    private LocalDateTime expiration;

    private AccessGroupDTO accessGroupDTO;

}
