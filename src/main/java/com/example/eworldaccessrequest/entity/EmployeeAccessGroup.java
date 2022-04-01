package com.example.eworldaccessrequest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCESS_GROUP_ID")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
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