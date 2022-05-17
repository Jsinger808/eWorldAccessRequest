package com.example.eworldaccessrequest.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity(name = "Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table (name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long ID;

    @Column
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = EAGER)
    private Collection<AppUserRole> appUserRoles = new ArrayList<>();


    public Role(Long ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "ID=" + ID +
                ", name='" + name +
                '}';
    }
}
