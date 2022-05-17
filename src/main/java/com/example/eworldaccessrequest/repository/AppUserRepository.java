package com.example.eworldaccessrequest.repository;

import com.example.eworldaccessrequest.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
}
