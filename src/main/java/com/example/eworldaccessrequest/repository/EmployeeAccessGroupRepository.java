package com.example.eworldaccessrequest.repository;

import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface EmployeeAccessGroupRepository extends JpaRepository<EmployeeAccessGroup, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Employee_Access_Group WHERE id = :id ;", nativeQuery = true)
    public void deleteEmployeeAccessGroupByIDCustom(@Param("id") Long employeeAccessGroupID);

}
