package com.example.eworldaccessrequest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "AccessGroup")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table (name = "Access_Group")
public class AccessGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private String type;

    @OneToMany(mappedBy = "accessGroup", cascade = CascadeType.ALL)
    List<EmployeeAccessGroup> employeeAccessGroups;

    @Override
    public String toString() {
        return "ID: " + this.ID + ", Name: " + this.name + ", Type: " + this.type;
    }
}