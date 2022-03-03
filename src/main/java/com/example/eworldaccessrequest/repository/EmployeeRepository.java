package com.example.eworldaccessrequest.repository;


import com.example.eworldaccessrequest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    public Employee findByEmail(@Param("EMAIL") String email);


    @Query(value = "SELECT EMPLOYEE.*\n" +
            "FROM EMPLOYEE_ACCESS_GROUP\n" +
            "JOIN ACCESS_GROUP\n" +
            "\tON ACCESS_GROUP.ID = EMPLOYEE_ACCESS_GROUP.ACCESS_GROUP_ID\n" +
            "JOIN EMPLOYEE\n" +
            "\tON EMPLOYEE.ID = EMPLOYEE_ACCESS_GROUP.EMPLOYEE_ID\n" +
            "WHERE ACCESS_GROUP.ID = :id ;", nativeQuery = true)
    public List<Employee> findEmployeesByAccessGroupID(@Param("id") Long accessGroupID);

    @Query(value = "SELECT EMPLOYEE.*\n" +
            "FROM EMPLOYEE_ACCESS_GROUP\n" +
            "JOIN ACCESS_GROUP\n" +
            "\tON ACCESS_GROUP.ID = EMPLOYEE_ACCESS_GROUP.ACCESS_GROUP_ID\n" +
            "JOIN EMPLOYEE\n" +
            "\tON EMPLOYEE.ID = EMPLOYEE_ACCESS_GROUP.EMPLOYEE_ID\n" +
            "WHERE EMPLOYEE_ACCESS_GROUP.EXPIRATION IS NOT NULL AND EMPLOYEE_ACCESS_GROUP.EXPIRATION <= :rightNow ;", nativeQuery = true)
    public List<Employee> findEmployeesWithExpiredDHSForms(@Param("rightNow") LocalDate rightNow);


    @Query(value = "SELECT EMPLOYEE.*\n" +
            "FROM EMPLOYEE_ACCESS_GROUP\n" +
            "JOIN ACCESS_GROUP\n" +
            "\tON ACCESS_GROUP.ID = EMPLOYEE_ACCESS_GROUP.ACCESS_GROUP_ID\n" +
            "JOIN EMPLOYEE\n" +
            "\tON EMPLOYEE.ID = EMPLOYEE_ACCESS_GROUP.EMPLOYEE_ID\n" +
            "WHERE EMPLOYEE_ACCESS_GROUP.EXPIRATION IS NOT NULL AND EMPLOYEE_ACCESS_GROUP.EXPIRATION >= :rightNow AND EMPLOYEE_ACCESS_GROUP.EXPIRATION <= :oneMonth ;", nativeQuery = true)
    public List<Employee> findEmployeesWithSoonToBeExpiredDHSFormsInOneMonth(@Param("rightNow") LocalDate rightNow, @Param("oneMonth") LocalDate oneMonth);

}

