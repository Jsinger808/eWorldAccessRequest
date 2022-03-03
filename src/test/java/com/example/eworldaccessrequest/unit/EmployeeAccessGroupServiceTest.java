package com.example.eworldaccessrequest.unit;
import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeAccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.repository.EmployeeAccessGroupRepository;
import com.example.eworldaccessrequest.repository.EmployeeRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class EmployeeAccessGroupServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);
    Long randomNumber2 = RandomUtils.nextLong(0, 30);
    Long randomNumber3 = RandomUtils.nextLong(30, 60);
    Long randomNumber4 = RandomUtils.nextLong(0, 30);

    @Mock
    private EmployeeAccessGroupRepository employeeAccessGroupRepository;

    @Mock
    private EmployeeRepository employeeRepository;

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
        AccessGroup accessGroup = new AccessGroup(randomNumber2, "Test-AD", "AD", new ArrayList<>());
        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber, employee, accessGroup, null);
        ArrayList<EmployeeAccessGroup> employeeAccessGroupArrayList = new ArrayList<EmployeeAccessGroup>();
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
        AccessGroup accessGroup = new AccessGroup(randomNumber2, "Test-AD", "AD", new ArrayList<>());
        AccessGroup accessGroupUpdated = new AccessGroup(randomNumber3, "Updated-DHS", "DHS_FORM", new ArrayList<>());
        AccessGroupDTO accessGroupUpdatedDTO = accessGroupService.convertToDto(accessGroupUpdated);

        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber4, employee, accessGroup, null);
        EmployeeAccessGroup actualUpdated = new EmployeeAccessGroup(randomNumber4, employee, accessGroupUpdated, rightNow);

        Mockito.doReturn(Optional.of(actual)).when(employeeAccessGroupRepository).findById(randomNumber4);
        Mockito.doReturn(actual).when(employeeAccessGroupRepository).save(actual);

        EmployeeAccessGroupDTO actualUpdatedDTO = employeeAccessGroupService.updateEmployeeAccessGroup(actualUpdated, randomNumber4);

        Mockito.verify(employeeAccessGroupRepository).save(actual);
        Assert.assertEquals(actualUpdatedDTO.getAccessGroupDTO(), accessGroupUpdatedDTO);
        Assert.assertEquals(actualUpdatedDTO.getExpiration(), rightNow);

    }

    @Test
    public void WhenUpdateEmployeeAccessGroup_GivenUpdatedAccessGroupAndExpiration_ShouldUpdateOnlyAccessGroupInEmployeeAccessGroup() {

        LocalDate rightNow = LocalDate.now();

        Employee employee = new Employee(randomNumber, "BugGirl YOGURT Johnson", "BUGGIRL@GMAIL.COM", true, true, new ArrayList<>());
        AccessGroup accessGroup = new AccessGroup(randomNumber2, "Test-AD", "AD", new ArrayList<>());
        AccessGroup accessGroupUpdated = new AccessGroup(randomNumber3, "Updated-SECURELINK", "SECURELINK", new ArrayList<>());
        AccessGroupDTO accessGroupUpdatedDTO = accessGroupService.convertToDto(accessGroupUpdated);

        EmployeeAccessGroup actual = new EmployeeAccessGroup(randomNumber4, employee, accessGroup, null);
        EmployeeAccessGroup actualUpdated = new EmployeeAccessGroup(randomNumber4, employee, accessGroupUpdated, rightNow);

        Mockito.doReturn(Optional.of(actual)).when(employeeAccessGroupRepository).findById(randomNumber4);
        Mockito.doReturn(actual).when(employeeAccessGroupRepository).save(actual);

        EmployeeAccessGroupDTO actualUpdatedDTO = employeeAccessGroupService.updateEmployeeAccessGroup(actualUpdated, randomNumber4);

        Mockito.verify(employeeAccessGroupRepository).save(actual);
        Assert.assertEquals(actualUpdatedDTO.getAccessGroupDTO(), accessGroupUpdatedDTO);
        Assert.assertEquals(actualUpdatedDTO.getExpiration(), null);

    }
}
