package com.example.eworldaccessrequest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table (name = "Employee_Access_Group")
public class EmployeeAccessGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "ACCESS_GROUP_ID")
    private AccessGroup accessGroup;

    @Column(name = "EXPIRATION")
    private LocalDateTime expiration;

    public String toString() {
        return "ID: " + this.ID + ", Name: " + this.getEmployee().getID() + ", Type: " + this.getAccessGroup().getID();
    }

}