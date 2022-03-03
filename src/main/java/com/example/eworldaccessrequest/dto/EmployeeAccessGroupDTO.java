package com.example.eworldaccessrequest.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Data
@Component
public class EmployeeAccessGroupDTO {

    private LocalDate expiration;

    private AccessGroupDTO accessGroupDTO;

}
