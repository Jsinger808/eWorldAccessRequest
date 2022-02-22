package com.example.eworldaccessrequest.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class EmployeeAccessGroupResponse {

    private String fullName;

    private String name;

    private String type;

    private LocalDateTime expiration;

}
