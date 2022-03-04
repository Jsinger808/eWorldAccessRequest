package com.example.eworldaccessrequest.repository;

import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface EmployeeAccessGroupRepository extends JpaRepository<EmployeeAccessGroup, Long> {

}
