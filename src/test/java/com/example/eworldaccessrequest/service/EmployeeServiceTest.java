package com.example.eworldaccessrequest.service;

import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.exception.InvalidEmailException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;
import com.example.eworldaccessrequest.repository.EmployeeRepository;
import com.example.eworldaccessrequest.service.EmployeeService;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
//import org.junit.jupiter.api.Test;


@SpringBootTest
public class EmployeeServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeAccessGroupRepository employeeAccessGroupRepository;

    @InjectMocks
    EmployeeService employeeService = new EmployeeService();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void WhenSaveEmployee_GivenUnformattedFullNameAndEmail_ShouldFormatFullNameAndEmail() throws EmptyStringException {

        Employee expected = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@eworldes.com", true, true, new ArrayList<>());
        EmployeeDTO expectedDTO = employeeService.convertToDto(expected);

        when(employeeRepository.save(any(Employee.class))).then(AdditionalAnswers.returnsFirstArg());


/*        doAnswer
                (invocation -> {
            ReflectionTestUtils.setField((Employee) invocation.getArgument(0), "ID", 6);
            return null;
        }).when(employeeRepository).save(expected);*/

        EmployeeDTO actualDTO = employeeService.saveEmployee(expectedDTO);

        System.out.println(actualDTO.getFullName());
        System.out.println(actualDTO.getEmail());

        Mockito.verify(employeeRepository).save(any(Employee.class));
        Assert.assertEquals("Buggirl Yogurt Johnson", actualDTO.getFullName()) ;
        Assert.assertEquals("buggirl@eworldes.com", actualDTO.getEmail());
    }

    @Test
    public void WhenUpdateEmployee_GivenEmptyOrCapitalizedInput_ShouldFormat() {

        Employee expected = new Employee(randomNumber, "Buggirl Yogurt Johnson", "buggirl@eworldes.com", true, true, new ArrayList<>());
        Employee updatedExpected = new Employee(randomNumber, "", "bUGgiRL@eworldes.com", false, true, new ArrayList<>());

        Mockito.doReturn(Optional.of(expected)).when(employeeRepository).findById(randomNumber);

        when(employeeRepository.save(any(Employee.class))).then(AdditionalAnswers.returnsFirstArg());

        EmployeeDTO expectedDTO = employeeService.convertToDto(expected);
        EmployeeDTO updatedExpectedDTO = employeeService.convertToDto(updatedExpected);

        EmployeeDTO actualDTO = employeeService.updateEmployee(updatedExpectedDTO, randomNumber);

        System.out.println(actualDTO);
        System.out.println(expectedDTO);

        Mockito.verify(employeeRepository).save(expected);
        Assert.assertEquals("Buggirl Yogurt Johnson", actualDTO.getFullName()) ;
        Assert.assertEquals("buggirl@eworldes.com", actualDTO.getEmail());
        Assert.assertEquals(false, actualDTO.isOffshore());
    }

    @Test(expected = EmptyStringException.class)
    public void WhenSaveEmployee_GivenEmptyStrings_ShouldThrowEmptyStringException() throws EmptyStringException {

        Employee expected = new Employee(randomNumber, "", "", true, true, new ArrayList<EmployeeAccessGroup>());
        EmployeeDTO expectedDTO = employeeService.convertToDto(expected);

        employeeService.saveEmployee(expectedDTO);

        Mockito.verify(employeeRepository, times(0)).save(expected);

    }

    @Test(expected = InvalidEmailException.class)
    public void WhenSaveEmployee_GivenNonEWorldEmail_ShouldThrowInvalidEmailException() throws InvalidEmailException {

        Employee expected = new Employee(randomNumber, "Bug Girl", "buggirl@buggirl.com", true, true, new ArrayList<EmployeeAccessGroup>());
        EmployeeDTO expectedDTO = employeeService.convertToDto(expected);

        employeeService.saveEmployee(expectedDTO);

        Mockito.verify(employeeRepository, times(0)).save(expected);

    }

}
