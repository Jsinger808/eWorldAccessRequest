package com.example.eworldaccessrequest.dto;

import com.example.eworldaccessrequest.entity.AccessGroup;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    private String fullName;

}
