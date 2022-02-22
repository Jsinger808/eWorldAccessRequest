package com.example.eworldaccessrequest.unit;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeRepository;
import com.example.eworldaccessrequest.service.EmployeeService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Optional;


@SpringBootTest
public class EmployeeServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService = new EmployeeService();

    @Test
    public void WhenUpdateEmployee_GivenNormalInput_ShouldWork() {

        Employee expected = new Employee(randomNumber, "Bug Girl", "Buggirl@eworldes.com", true, true, new ArrayList<AccessGroup>());

        Mockito.doReturn(Optional.of(expected)).when(employeeRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(employeeRepository).save(expected);

        Employee actual = employeeService.updateEmployee(expected, randomNumber);

        System.out.println(actual.getID());
        System.out.println(expected.getID());

        Assert.assertEquals(actual.getID(), expected.getID());
    }

    @Test
    public void WhenUpdateEmployee_GivenEmptyFullNameAndEmail_ShouldNotUpdateThoseFieldsButStillWork() {

        Employee expected = new Employee(randomNumber, "", "", true, true, new ArrayList<AccessGroup>());

        Mockito.doReturn(Optional.of(expected)).when(employeeRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(employeeRepository).save(expected);

        Employee actual = employeeService.updateEmployee(expected, randomNumber);
        System.out.println(actual.getID());
        System.out.println(expected.getID());

        Assert.assertEquals(actual.getID(), expected.getID());
    }

    @Test
    public void WhenSaveEmployee_GivenUnformattedFullNameAndEmail_ShouldFormatFullNameAndEmail() throws EmptyStringException{

        Employee expected = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<AccessGroup>());

        Mockito.doReturn(expected).when(employeeRepository).save(expected);

        Employee actual = employeeService.saveEmployee(expected);
        System.out.println(actual.getFullName());
        System.out.println(actual.getEmail());

        Assert.assertEquals(actual.getFullName(), "Buggirl Yogurt Johnson");
        Assert.assertEquals(actual.getEmail(), "buggirl@gmail.com");
    }

    @Test
    public void WhenSaveEmployee_GivenEmptyString_ShouldThrowEmptyStringException() throws EmptyStringException{

        Employee expected = new Employee(randomNumber, "", "", true, true, new ArrayList<AccessGroup>());

        Mockito.doReturn(expected).when(employeeRepository).save(expected);

        Employee actual = employeeService.saveEmployee(expected);



    }

}
