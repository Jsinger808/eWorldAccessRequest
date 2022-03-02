package com.example.eworldaccessrequest.unit;

import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.exception.InvalidEmailException;
import com.example.eworldaccessrequest.repository.EmployeeRepository;
import com.example.eworldaccessrequest.service.EmployeeService;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.times;
//import org.junit.jupiter.api.Test;


@SpringBootTest
public class EmployeeServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService = new EmployeeService();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void WhenSaveEmployee_GivenUnformattedFullNameAndEmail_ShouldFormatFullNameAndEmail() throws EmptyStringException {

        Employee expected = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<>());

        Mockito.doReturn(expected).when(employeeRepository).save(expected);

        EmployeeDTO actual = employeeService.saveEmployee(expected);

        System.out.println(actual.getFullName());
        System.out.println(actual.getEmail());

        Mockito.verify(employeeRepository).save(expected);
        Assert.assertEquals(actual.getFullName(), "Buggirl Yogurt Johnson");
        Assert.assertEquals(actual.getEmail(), "buggirl@gmail.com");
    }

    @Test
    public void WhenUpdateEmployee_GivenCapitalizedInput_ShouldFormat() {

        Employee expected = new Employee(randomNumber, "Bug Girl", "buggirl@eworldes.com", true, true, new ArrayList<>());

        Mockito.doReturn(Optional.of(expected)).when(employeeRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(employeeRepository).save(expected);
        EmployeeDTO expectedDTO = employeeService.convertToDto(expected);

        EmployeeDTO actual = employeeService.updateEmployee(new Employee(randomNumber, "BUg GIRl", "BUGGIRL@eworldes.com",
                true, true, new ArrayList<>()), randomNumber);

        System.out.println(actual);
        System.out.println(expectedDTO);

        Mockito.verify(employeeRepository).save(expected);
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void WhenUpdateEmployee_GivenEmptyEmail_ShouldUpdateFullNameButNotUpdateEmail() {

        Employee expected = new Employee(randomNumber, "Buggie Girl", "buggirl@eworldes.com",
                true, true, new ArrayList<>());

        Mockito.doReturn(Optional.of(expected)).when(employeeRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(employeeRepository).save(expected);
        EmployeeDTO expectedDTO = employeeService.convertToDto(expected);

        EmployeeDTO actual = employeeService.updateEmployee(new Employee(randomNumber, "Wonderbug", "", true,
                true, new ArrayList<>()), randomNumber);

        System.out.println(actual);
        System.out.println(expectedDTO);

        Mockito.verify(employeeRepository).save(expected);
        Assert.assertEquals(actual.getFullName(), "Wonderbug");
        Assert.assertEquals(actual.getEmail(), "buggirl@eworldes.com");

    }


    @Test(expected = EmptyStringException.class)
    public void WhenSaveEmployee_GivenEmptyStrings_ShouldThrowEmptyStringException() throws EmptyStringException {

        Employee expected = new Employee(randomNumber, "", "", true, true, new ArrayList<EmployeeAccessGroup>());
        employeeService.saveEmployee(expected);

        Mockito.verify(employeeRepository, times(0)).save(expected);

    }

    @Test(expected = InvalidEmailException.class)
    public void WhenSaveEmployee_GivenNonEWorldEmail_ShouldThrowInvalidEmailException() throws EmptyStringException {

        Employee expected = new Employee(randomNumber, "Bug Girl", "buggirl@buggirl.com", true, true, new ArrayList<EmployeeAccessGroup>());
        employeeService.saveEmployee(expected);

        Mockito.verify(employeeRepository, times(0)).save(expected);

    }

}
