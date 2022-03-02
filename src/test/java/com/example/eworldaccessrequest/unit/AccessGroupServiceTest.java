package com.example.eworldaccessrequest.unit;

import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.exception.InvalidAccessGroupTypeException;
import com.example.eworldaccessrequest.repository.AccessGroupRepository;
import com.example.eworldaccessrequest.service.AccessGroupService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
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


@SpringBootTest
public class AccessGroupServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);
    Long randomNumber2 = RandomUtils.nextLong(0, 30);

    @Mock
    private AccessGroupRepository accessGroupRepository;

    @InjectMocks
    AccessGroupService accessGroupService = new AccessGroupService();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void WhenSaveAccessGroup_GivenNormalInput_ShouldSave() {

        AccessGroup expected = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<EmployeeAccessGroup>());
        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);
        AccessGroupDTO expectedDTO = accessGroupService.convertToDto(expected);

        AccessGroupDTO actual = accessGroupService.saveAccessGroup(expected);

        Mockito.verify(accessGroupRepository).save(expected);
        Assert.assertEquals(actual, expectedDTO);

    }

    @Test(expected = EmptyStringException.class)
    public void WhenSaveAccessGroup_GivenEmptyString_ShouldThrowEmptyStringException() throws EmptyStringException {

        AccessGroup expected = new AccessGroup(randomNumber, "", "AD", new ArrayList<EmployeeAccessGroup>());
        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);
        AccessGroupDTO expectedDTO = accessGroupService.convertToDto(expected);

        AccessGroupDTO actual = accessGroupService.saveAccessGroup(expected);

        Mockito.verify(accessGroupRepository).save(expected);

    }

    @Test(expected = InvalidAccessGroupTypeException.class)
    public void WhenSaveAccessGroup_GivenAnIncorrectType_ShouldThrowInvalidAccessGroupTypeException() throws InvalidAccessGroupTypeException {

        AccessGroup expected = new AccessGroup(randomNumber, "Test-AD", "ADD", new ArrayList<EmployeeAccessGroup>());
        accessGroupService.saveAccessGroup(expected);

        Mockito.verify(accessGroupRepository, times(0)).save(expected);

    }

    @Test
    public void WhenUpdateAccessGroup_GivenEmptyType_ShouldUpdateNameButNotUpdateType() throws EmptyStringException {

        AccessGroup expected = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<EmployeeAccessGroup>());


        Mockito.doReturn(Optional.of(expected)).when(accessGroupRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);
        AccessGroupDTO expectedDTO = accessGroupService.convertToDto(expected);

        AccessGroupDTO actual = accessGroupService.updateAccessGroup((new AccessGroup(randomNumber, "Stuff", "",
                new ArrayList<EmployeeAccessGroup>())), randomNumber);

        Mockito.verify(accessGroupRepository).save(expected);

        Assert.assertEquals(actual.getName(), "Stuff");
        Assert.assertEquals(actual.getType(), "AD");

    }

    @Test
    public void WhenUpdateAccessGroup_GivenNormalInput_ShouldUpdateNameAndType() throws EmptyStringException {

        AccessGroup expected = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<EmployeeAccessGroup>());

        Mockito.doReturn(Optional.of(expected)).when(accessGroupRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);
        AccessGroupDTO expectedDTO = accessGroupService.convertToDto(expected);

        AccessGroupDTO actual = accessGroupService.updateAccessGroup((new AccessGroup(randomNumber, "Stuff", "SECURELINK",
                new ArrayList<EmployeeAccessGroup>())), randomNumber);

        Mockito.verify(accessGroupRepository).save(expected);

        Assert.assertEquals(actual.getName(), "Stuff");
        Assert.assertEquals(actual.getType(), "SECURELINK");

    }




}
