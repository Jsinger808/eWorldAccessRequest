package com.example.eworldaccessrequest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(unique = true, name = "EMAIL")
    private String email;

    @Column(name = "OFFSHORE")
    private boolean offshore;

    @Column(name = "BES")
    private boolean bes;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @JsonBackReference
    @JsonManagedReference
    List<EmployeeAccessGroup> employeeAccessGroups;

    public Employee(String fullName, String email, boolean offshore, boolean bes, List<EmployeeAccessGroup> employeeAccessGroups) {
        this.fullName = fullName;
        this.email = email;
        this.offshore = offshore;
        this.bes = bes;
        this.employeeAccessGroups = employeeAccessGroups;
    }

    @Override
    public String toString() {
        return "ID: " + this.ID + ", Full Name: " + this.fullName + ", Email: " + this.email + ", Offshore: " + this.offshore + ", BES: " + this.bes;
    }


}

