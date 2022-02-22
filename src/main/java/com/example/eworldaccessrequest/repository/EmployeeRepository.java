package com.example.eworldaccessrequest.repository;


import com.example.eworldaccessrequest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    @Query(
//            value = "SELECT EMPLOYEE.ID, EMPLOYEE.FULL_NAME, EMPLOYEE.US_RESIDENT, EMPLOYEE.FULL_TIME " +
//                    "FROM EMPLOYEE;", nativeQuery = true
//    )
//    public List<Employee> findBingus();

    public Employee findByEmail(@Param("EMAIL") String email);

}

