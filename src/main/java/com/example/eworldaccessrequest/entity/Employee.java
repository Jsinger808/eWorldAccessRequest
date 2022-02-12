package com.example.eworldaccessrequest.entity;

// Importing required classes


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)

        // Rename columns in MySQL
        @Column(name = "ID")
        private Long employeeId;

        @Column(name = "FULL_NAME")
        private String fullName;

        @Column(name = "EMAIL")
        private String email;

        @Column(name = "US_RESIDENT")
        private boolean usResident;

        @Column(name = "FULL_TIME")
        private boolean fullTime;

}

