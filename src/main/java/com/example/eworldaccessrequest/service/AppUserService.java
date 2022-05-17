package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.*;
import com.example.eworldaccessrequest.entity.AppUser;
import com.example.eworldaccessrequest.entity.AppUserRole;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.entity.Role;

import com.example.eworldaccessrequest.repository.AppUserRepository;
import com.example.eworldaccessrequest.repository.RoleRepository;
import com.example.eworldaccessrequest.security.AccessAppPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@Slf4j
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // DTO Converter
    public static AppUserDTO convertToDto(AppUser appUser) {

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setPassword(appUser.getPassword());
        appUser.getAppUserRoles();
        ArrayList appUserRoleDTOs = new ArrayList();
        if (appUser.getAppUserRoles() == null || appUser.getAppUserRoles().isEmpty()) {
            appUser.setAppUserRoles(appUserRoleDTOs);
        } else {
            for (AppUserRole appUserRole : appUser.getAppUserRoles()) {
                appUserRoleDTOs.add(AppUserService.convertAppUserRoleToDto(appUserRole));
            }
            appUserDTO.setAppUserRoleDTOs(appUserRoleDTOs);
        }

        return appUserDTO;
    }

    public static AppUserRoleDTO convertAppUserRoleToDto(AppUserRole appUserRole) {
        AppUserRoleDTO appUserRoleDTO = new AppUserRoleDTO();
        appUserRoleDTO.setRoleDTO(AppUserService.convertRoleToDto(appUserRole.getRole()));
        return appUserRoleDTO;
    }

    public static RoleDTO convertRoleToDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setID(roleDTO.getID());
        roleDTO.setName(roleDTO.getName());
        return roleDTO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser == null) {
            throw new UsernameNotFoundException("User not found in database");
        }
        else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            appUser.getAppUserRoles().forEach(appUserRole -> authorities.add(new SimpleGrantedAuthority(appUserRole.getRole().getName())));
            return new User(appUser.getUsername(), appUser.getPassword(), authorities);
        }
    }

    // Save operation
    public AppUserDTO saveUser(AppUserDTO appUserDTO) {
        log.info("Saving new user {} to the database", appUserDTO.getUsername());
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        return convertToDto(appUserRepository.save(appUser));
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser (String username, String roleName) {
        log.info("Saving role {} to user {}", roleName, username);
        AppUser appUser = appUserRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        AppUserRole appUserRole = new AppUserRole(appUser, role);
        appUser.getAppUserRoles().add(appUserRole);
        appUserRepository.save(appUser);
    }

    public AppUser fetchUserByUsername(String username) {
        log.info("Fetching user {}", username);
        return appUserRepository.findByUsername(username);
    }

    // Read operation
//    public void checkUserPassword(AppUserDTO appUserDTO) throws Exception {
//        if (appUserRepository.findByUsername(appUserDTO.getUsername()) == null) {
//            throw new RuntimeException();
//        }
//        User user = appUserRepository.findByUsername(appUserDTO.getUsername());
//        if (saltPassword(hashPassword(appUserDTO.getPassword()), user.getSalt()) == user.getPassword()) {
//            return JWTtoken();
//        }
//        else {
//            throw new RuntimeException();
//        }
//    }


}
