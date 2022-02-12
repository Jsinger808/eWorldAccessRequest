package com.example.eworldaccessrequest.repository;

// Importing required classes
import com.example.eworldaccessrequest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
