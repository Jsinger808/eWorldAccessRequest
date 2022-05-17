package com.example.eworldaccessrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {

    private String username;

    private String password;

    private List<Long> userRoleIDs;

    private List<AppUserRoleDTO> appUserRoleDTOs;

    public AppUserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
