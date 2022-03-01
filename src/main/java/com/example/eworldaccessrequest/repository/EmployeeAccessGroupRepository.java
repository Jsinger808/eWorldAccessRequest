package com.example.eworldaccessrequest.repository;

import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EmployeeAccessGroupRepository extends JpaRepository<EmployeeAccessGroup, Long> {


}
