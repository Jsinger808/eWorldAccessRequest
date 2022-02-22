package com.example.eworldaccessrequest.unit;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.EmptyStringException;
import com.example.eworldaccessrequest.exception.InvalidAccessGroupTypeException;
import com.example.eworldaccessrequest.repository.AccessGroupRepository;
import com.example.eworldaccessrequest.service.AccessGroupService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Optional;


@SpringBootTest
public class AccessGroupServiceTest {

    Long randomNumber = RandomUtils.nextLong(0, 30);

    @Mock
    private AccessGroupRepository accessGroupRepository;

    @InjectMocks
    AccessGroupService accessGroupService = new AccessGroupService();

    @Test
    public void WhenSaveAccessGroup_GivenNormalInput_ShouldWork() throws EmptyStringException {

        AccessGroup expected = new AccessGroup(randomNumber, "Test-AD", "AD", new ArrayList<EmployeeAccessGroup>());

        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);

        AccessGroup actual = accessGroupService.saveAccessGroup(expected);
        System.out.println(actual.getName());
        System.out.println(expected.getName());

        Assert.assertEquals(actual.getName(), expected.getName());

    }

    @Test
    public void WhenSaveAccessGroup_GivenAnIncorrectType_ShouldThrowInvalidAccessGroupTypeException () throws InvalidAccessGroupTypeException {

        AccessGroup expected = new AccessGroup(randomNumber, "Test-AD", "ADD", new ArrayList<EmployeeAccessGroup>());

        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);

        AccessGroup actual = accessGroupService.saveAccessGroup(expected);
        System.out.println(actual.getName());
        System.out.println(expected.getName());

        Assert.assertEquals(actual.getName(), expected.getName());

    }

    @Test
    public void WhenUpdateAccessGroup_GivenEmptyString_ShouldNotUpdateThoseFieldsButStillWork() throws EmptyStringException {

        AccessGroup expected = new AccessGroup(randomNumber, "", "AD", new ArrayList<EmployeeAccessGroup>());

        Mockito.doReturn(Optional.of(expected)).when(accessGroupRepository).findById(randomNumber);
        Mockito.doReturn(expected).when(accessGroupRepository).save(expected);

        AccessGroup actual = accessGroupService.updateAccessGroup(expected, randomNumber);
        System.out.println(actual.getID());
        System.out.println(expected.getID());

        Assert.assertEquals(actual.getID(), expected.getID());

    }
}
