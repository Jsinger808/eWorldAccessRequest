package com.example.eworldaccessrequest.repository;

import com.example.eworldaccessrequest.entity.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long> {

}
