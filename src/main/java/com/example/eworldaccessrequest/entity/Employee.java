package com.example.eworldaccessrequest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
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

    @Column(name = "US_RESIDENT")
    private boolean usResident;

    @Column(name = "FULL_TIME")
    private boolean fullTime;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Employee_Access_Group",
            joinColumns = {@JoinColumn(name = "EMPLOYEE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ACCESS_GROUP_ID")}
    )
    private List<AccessGroup> accessGroups = new ArrayList<>();

}

