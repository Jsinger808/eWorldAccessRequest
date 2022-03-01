package com.example.eworldaccessrequest.repository;


import com.example.eworldaccessrequest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    public Employee findByEmail(@Param("EMAIL") String email);

}

