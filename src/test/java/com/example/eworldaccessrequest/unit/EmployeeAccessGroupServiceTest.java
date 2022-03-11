package com.example.eworldaccessrequest.unit;
import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeAccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;
import com.example.eworldaccessrequest.service.AccessGroupService;
import com.example.eworldaccessrequest.service.EmployeeAccessGroupService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.times;

@SpringBootTest
public class EmployeeAccessGroupServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);
    Long randomNumber2 = RandomUtils.nextLong(30, 60);
    Long randomNumber3 = RandomUtils.nextLong(0, 30);

    @Mock
    private EmployeeAccessGroupRepository employeeAccessGroupRepository;

    @InjectMocks
    EmployeeAccessGroupService employeeAccessGroupService = new EmployeeAccessGroupService();

    @InjectMocks
    EmployeeService employeeService = new EmployeeService();

    @InjectMocks
    AccessGroupService accessGroupService = new AccessGroupService();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void WhenSaveEmployeeAccessGroup_GivenNormalInput_ShouldReferenceEmployeeToAccessGroup() throws EmptyStringException {

        Employee employee = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<>());
        AccessGroup accessGroup = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<>());
        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber, employee, accessGroup, null);
        ArrayList<EmployeeAccessGroup> employeeAccessGroupArrayList = new ArrayList<>();
        employeeAccessGroupArrayList.add(actual);

        Mockito.doReturn(actual).when(employeeAccessGroupRepository).save(actual);

        EmployeeAccessGroupDTO actualDTO = employeeAccessGroupService.saveEmployeeAccessGroup(actual);
        employee.setEmployeeAccessGroups(employeeAccessGroupArrayList);

        EmployeeDTO employeeDTO = employeeService.convertToDto(employee);
        AccessGroupDTO accessGroupDTO = accessGroupService.convertToDto(accessGroup);

        Mockito.verify(employeeAccessGroupRepository).save(actual);
        Assert.assertEquals(null, actualDTO.getExpiration());
        Assert.assertEquals(accessGroupDTO, actualDTO.getAccessGroupDTO());
        Assert.assertEquals(actual.getEmployee(), employee);
        Assert.assertEquals(actual.getAccessGroup(), accessGroup);
        Assert.assertTrue(employeeDTO.getEmployeeAccessGroupDTOs().contains(actualDTO));
    }

    @Test
    public void WhenUpdateEmployeeAccessGroup_GivenUpdatedAccessGroupToDHSFormAndExpiration_ShouldUpdateBothInEmployeeAccessGroup() {

        LocalDate rightNow = LocalDate.now();

        Employee employee = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<>());
        AccessGroup accessGroup = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<>());
        AccessGroup accessGroupUpdated = new AccessGroup(randomNumber2, "Updated-DHS", "DHS_FORM", new ArrayList<>());
        AccessGroupDTO accessGroupUpdatedDTO = accessGroupService.convertToDto(accessGroupUpdated);

        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber3, employee, accessGroup, null);
        EmployeeAccessGroup actualUpdated = new EmployeeAccessGroup(randomNumber3, employee, accessGroupUpdated, rightNow);

        Mockito.doReturn(Optional.of(actual)).when(employeeAccessGroupRepository).findById(randomNumber3);
        Mockito.doReturn(actual).when(employeeAccessGroupRepository).save(actual);

        EmployeeAccessGroupDTO actualUpdatedDTO = employeeAccessGroupService.updateEmployeeAccessGroup(actualUpdated, randomNumber3);

        Mockito.verify(employeeAccessGroupRepository).save(actual);
        Assert.assertEquals(accessGroupUpdatedDTO, actualUpdatedDTO.getAccessGroupDTO());
        Assert.assertEquals(rightNow, actualUpdatedDTO.getExpiration()) ;

    }

    @Test
    public void WhenUpdateEmployeeAccessGroup_GivenUpdatedAccessGroupAndExpiration_ShouldUpdateOnlyAccessGroupInEmployeeAccessGroup() {

        LocalDate rightNow = LocalDate.now();

        Employee employee = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<>());
        AccessGroup accessGroup = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<>());
        AccessGroup accessGroupUpdated = new AccessGroup(randomNumber2, "Updated-SECURELINK", "SECURELINK", new ArrayList<>());
        AccessGroupDTO accessGroupUpdatedDTO = accessGroupService.convertToDto(accessGroupUpdated);

        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber3, employee, accessGroup, null);
        EmployeeAccessGroup actualUpdated = new EmployeeAccessGroup(randomNumber3, employee, accessGroupUpdated, rightNow);

        Mockito.doReturn(Optional.of(actual)).when(employeeAccessGroupRepository).findById(randomNumber3);
        Mockito.doReturn(actual).when(employeeAccessGroupRepository).save(actual);

        EmployeeAccessGroupDTO actualUpdatedDTO = employeeAccessGroupService.updateEmployeeAccessGroup(actualUpdated, randomNumber3);

        Mockito.verify(employeeAccessGroupRepository).save(actual);
        Assert.assertEquals(accessGroupUpdatedDTO, actualUpdatedDTO.getAccessGroupDTO());
        Assert.assertEquals(null, actualUpdatedDTO.getExpiration());

    }

    @Test (expected = EmptyStringException.class)
    public void WhenSaveEmployeeAccessGroup_GivenEmployeeWithEmptyFullName_ShouldThrowEmptyStringException() throws EmptyStringException {

        Employee employee = new Employee(randomNumber, "", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<>());
        AccessGroup accessGroup = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<>());
        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber, employee, accessGroup, null);
        ArrayList<EmployeeAccessGroup> employeeAccessGroupArrayList = new ArrayList<EmployeeAccessGroup>();
        employeeAccessGroupArrayList.add(actual);

        Mockito.doReturn(actual).when(employeeAccessGroupRepository).save(actual);

        EmployeeAccessGroupDTO actualDTO = employeeAccessGroupService.saveEmployeeAccessGroup(actual);
        employee.setEmployeeAccessGroups(employeeAccessGroupArrayList);

        EmployeeDTO employeeDTO = employeeService.convertToDto(employee);
        AccessGroupDTO accessGroupDTO = accessGroupService.convertToDto(accessGroup);

        Mockito.verify(employeeAccessGroupRepository, times(0)).save(actual);
        Assert.assertEquals(null, actualDTO.getExpiration());
        Assert.assertEquals(accessGroupDTO, actualDTO.getAccessGroupDTO());
        Assert.assertEquals(employee, actual.getEmployee());
        Assert.assertEquals(accessGroup, actual.getAccessGroup());
        Assert.assertTrue(employeeDTO.getEmployeeAccessGroupDTOs().contains(actualDTO));
    }
}
