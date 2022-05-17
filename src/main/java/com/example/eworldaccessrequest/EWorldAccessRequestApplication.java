package com.example.eworldaccessrequest;

import com.example.eworldaccessrequest.dto.AppUserDTO;
import com.example.eworldaccessrequest.entity.AppUser;
import com.example.eworldaccessrequest.entity.Role;
import com.example.eworldaccessrequest.security.AccessAppPasswordEncoder;
import com.example.eworldaccessrequest.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;


@SpringBootApplication
public class EWorldAccessRequestApplication {


    public static void main(String[] args) {
        SpringApplication.run(EWorldAccessRequestApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(AppUserService appUserService) {
//        return args -> {
//            appUserService.saveRole(new Role(null, "ROLE_GENERAL2"));
//            appUserService.saveRole(new Role(null, "ROLE_ADMIN3"));
//
//            appUserService.saveUser(new AppUserDTO("Dave", "1234"));
//
//            appUserService.addRoleToUser("Johnny Bens", "ROLE_ADMIN2");
//        };
//    }


    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


