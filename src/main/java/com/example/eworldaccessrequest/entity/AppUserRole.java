package com.example.eworldaccessrequest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Entity(name = "AppUserRole")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table (name = "AppUser_Role")
public class AppUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPUSER_ID")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Role role;

    public AppUserRole(AppUser appUser, Role role) {
        this.appUser = appUser;
        this.role = role;
    }

    @Override
    public String toString() {
        return "AppUserRole{" +
                "ID=" + ID +
                ", appUser=" + appUser.getID() +
                ", role=" + role.getID() +
                '}';
    }

}