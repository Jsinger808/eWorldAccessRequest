package com.example.eworldaccessrequest.dto;

import lombok.Data;
import org.springframework.stereotype.Component;



@Data
@Component
public class AccessGroupDTO {

    private Long ID;

    private String name;

    private String type;

}
