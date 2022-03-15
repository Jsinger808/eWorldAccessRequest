package com.example.eworldaccessrequest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class EWorldAccessRequestApplication {


    public static void main(String[] args) {
        SpringApplication.run(EWorldAccessRequestApplication.class, args);
    }

}
