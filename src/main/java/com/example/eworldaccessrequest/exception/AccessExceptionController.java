package com.example.eworldaccessrequest.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class AccessExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException){
        return new ResponseEntity<>("Duplicate entry.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyStringException.class)
    public ResponseEntity<Object> handleEmptyStringException(EmptyStringException emptyStringException){
        return new ResponseEntity<>("Empty String. Please input a value.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyIDException.class)
    public ResponseEntity<Object> handleEmptyIDException(EmptyIDException emptyIDException){
        return new ResponseEntity<>("Empty ID. Please input a value.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccessGroupTypeException.class)
    public ResponseEntity<Object> handleInvalidAccessGroupTypeException(InvalidAccessGroupTypeException invalidAccessGroupTypeException){
        return new ResponseEntity<>("Invalid Access Group Type. Please input either AD, SECURELINK, OUTLOOK_EMAIL, or DHS_FORM.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handleEmployeeNotFoundException(EmployeeNotFoundException employeeNotFoundException){
        return new ResponseEntity<>("Employee not found. Please re-enter a new value.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Object> handleInvalidEmailException(InvalidEmailException invalidEmailException){
        return new ResponseEntity<>("Non-eWorld email entered. Please enter an eWorld email address.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoEmployeesWithExpiredAccessGroupsException.class)
    public ResponseEntity<Object> handleNoEmployeesWithExpiredAccessGroupsException(NoEmployeesWithExpiredAccessGroupsException noEmployeesWithExpiredAccessGroupsException){
        return new ResponseEntity<>("No Employees with expired forms.", HttpStatus.OK);
    }

    @ExceptionHandler(NoEmployeesWithSoonToBeExpiredAccessGroupsException.class)
    public ResponseEntity<Object> handleNoEmployeesWithSoonToBeExpiredAccessGroupsException(NoEmployeesWithSoonToBeExpiredAccessGroupsException noEmployeesWithSoonToBeExpiredAccessGroupsException){
        return new ResponseEntity<>("No Employees with soon-to-be expired forms.", HttpStatus.OK);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException sQLException){
        return new ResponseEntity<>("SQL Error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
