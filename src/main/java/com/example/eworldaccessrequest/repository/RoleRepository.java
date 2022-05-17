package com.example.eworldaccessrequest.repository;

import com.example.eworldaccessrequest.entity.AppUser;
import com.example.eworldaccessrequest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName (String name);
}
