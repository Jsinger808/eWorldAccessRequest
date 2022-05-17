package com.example.eworldaccessrequest.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@Entity(name = "AppUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table (name = "AppUser")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Collection<AppUserRole> appUserRoles = new ArrayList<>();

    public AppUser(String username, String password, Collection<AppUserRole> appUserRoles) {
        this.username = username;
        this.password = password;
        this.appUserRoles = appUserRoles;
    }

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", appUserRoles=" + appUserRoles +
                '}';
    }
}