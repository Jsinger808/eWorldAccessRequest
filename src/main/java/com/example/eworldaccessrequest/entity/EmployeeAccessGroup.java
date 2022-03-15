package com.example.eworldaccessrequest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "EmployeeAccessGroup")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table (name = "Employee_Access_Group")
public class EmployeeAccessGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

//    @Column(name = "EMPLOYEE_ID")
//    private Long employeeID;
//
//    @Column(name = "ACCESS_GROUP_ID")
//    private Long accessGroupID;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "EMPLOYEE_ID")
//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnore
    private Employee employee;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ACCESS_GROUP_ID")
//    @JsonBackReference
//    @JsonManagedReference
    private AccessGroup accessGroup;

    @Column(name = "EXPIRATION")
    private LocalDate expiration;

    public EmployeeAccessGroup(Employee employee, AccessGroup accessGroup, LocalDate expiration) {
        this.employee = employee;
        this.accessGroup = accessGroup;
        this.expiration = expiration;
    }

//    public EmployeeAccessGroup(Long ID, Employee employee, AccessGroup accessGroup, LocalDate expiration) {
//        this.ID = ID;
//        this.employee = employee;
//        this.accessGroup = accessGroup;
//        this.expiration = expiration;
//    }

//    public EmployeeAccessGroup(Long employeeID, Long accessGroupID, LocalDate expiration) {
//        this.employeeID = employeeID;
//        this.accessGroupID = accessGroupID;
//        this.expiration = expiration;
//    }
//
//    public EmployeeAccessGroup(Long ID, Long employeeID, Long accessGroupID, LocalDate expiration) {
//        this.ID = ID;
//        this.employeeID = employeeID;
//        this.accessGroupID = accessGroupID;
//        this.expiration = expiration;
//    }


//    @Override
//    public String toString() {
//        return "ID: " + this.ID + ", Name: " + this.getEmployee().getID() + ", Type: " + this.getAccessGroup().getID();
//    }


}