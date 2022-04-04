
package com.example.eworldaccessrequest.integration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.eworldaccessrequest.EWorldAccessRequestApplication;
import com.example.eworldaccessrequest.controller.AccessController;
import com.example.eworldaccessrequest.dto.AccessGroupDTO;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.AccessGroup;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.entity.EmployeeAccessGroup;
import com.example.eworldaccessrequest.exception.InvalidAccessGroupTypeException;
import com.example.eworldaccessrequest.service.EmployeeService;
import com.example.eworldaccessrequest.service.AccessGroupService;
import com.example.eworldaccessrequest.service.EmployeeAccessGroupService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.*;


import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(classes = EWorldAccessRequestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
//@Transactional
//@TestExecutionListeners({
//        DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class,
//        DbUnitTestExecutionListener.class
//})
//@WebMvcTest(AccessController.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@TestPropertySource("classpath:/application-test.properties")
public class AccessControllerIntegrationTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AccessGroupService accessGroupService;
    @Autowired
    private EmployeeAccessGroupService employeeAccessGroupService;
    @Autowired
    private AccessController accessController;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;



    Long randomNumber = RandomUtils.nextLong(0, 30);
    Long randomNumber2 = RandomUtils.nextLong(30, 60);


//Employee APIs

    //Posts
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void  WhenPostEmployee_GivenNormalEmployee_ShouldStatus200() throws Exception {

        Employee expected = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName", Matchers.is("Johnny Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email", Matchers.is("johnnysmith@eworldes.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].offshore", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].bes", Matchers.is(true)));

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenPostEmployee_GivenEmployeeWithSameEmail_ShouldThrowDataIntegrityViolationException() throws Exception {

        Employee expected = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());
        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName").exists());

        Employee expected2 = new Employee(randomNumber, "Frank Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataIntegrityViolationException))
                .andExpect(content().string("Duplicate entry."));

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenPostEmployee_GivenEmployeeWithEmployeeAccessGroup_ShouldStatus200() throws Exception {

        Employee expectedEmployee = new Employee((long) 1,"Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        AccessGroup expectedAccessGroup = new AccessGroup((long) 1, "Test-DHS_FORM", "DHS_FORM", new ArrayList<>());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        LocalDate rightNow = LocalDate.now();
        rightNow.toString();

        EmployeeAccessGroup expectedEmployeeAccessGroup = new EmployeeAccessGroup((long) 1, expectedEmployee, expectedAccessGroup, rightNow);

        expectedEmployee.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeAccessGroupDTOs.[0].expiration", Matchers.is(rightNow.toString())));
    }

    //Gets
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void  WhenGetEmployeeByEmail_GivenExistingEmployeeEmail_ShouldReturnOnlyOneEmployeeWithStatus200() throws Exception {

        Employee expected = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        Employee expected2 = new Employee("Howard Duck", "howardduck@eworldes.com",
                true, true, new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee/" + expected.getEmail())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName", Matchers.is("Johnny Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("johnnysmith@eworldes.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.offshore", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bes", Matchers.is(true)));

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenGetEmployeeByAccessGroupID1_GivenOnlyOneEmployeeInAccessGroupID1_ShouldReturnOnlyOneEmployeeWithStatus200() throws Exception {

        Employee expectedEmployee = new Employee((long) 1, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        Employee expectedEmployee2 = new Employee((long) 2, "Bug Girl", "burgirl@eworldes.com",
                true, true, new ArrayList<>());

        AccessGroup expectedAccessGroup = new AccessGroup((long) 1, "Test-DHS_FORM", "DHS_FORM", new ArrayList<>());

        AccessGroup expectedAccessGroup2 = new AccessGroup((long) 2, "Test-AD", "AD", new ArrayList<>());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup2)))
                .andDo(print())
                .andExpect(status().isOk());

        EmployeeAccessGroup expectedEmployeeAccessGroup = new EmployeeAccessGroup((long) 1, expectedEmployee, expectedAccessGroup, null);

        expectedEmployee.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

        EmployeeAccessGroup expectedEmployeeAccessGroup2 = new EmployeeAccessGroup((long) 2, expectedEmployee2, expectedAccessGroup2, null);
        expectedEmployee2.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup2);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee2)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/access/employee/accessGroup/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName", Matchers.is("Johnny Smith")));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenGetEmployeeByExpired_GivenEmployeeWithPastExpirationDate_ShouldReturnOnlyExpiredEmployeeWithStatus200() throws Exception {

        Employee expectedEmployee = new Employee((long) 1, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        Employee expectedEmployee2 = new Employee((long) 2, "Bug Girl", "burgirl@eworldes.com",
                true, true, new ArrayList<>());

        AccessGroup expectedAccessGroup = new AccessGroup((long) 1, "Test-DHS_FORM", "DHS_FORM", new ArrayList<>());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        LocalDate rightNow = LocalDate.now();
        LocalDate monthAgo = rightNow.minusMonths(1);
        LocalDate monthFromNow = rightNow.plusMonths(1);

        EmployeeAccessGroup expectedEmployeeAccessGroup = new EmployeeAccessGroup((long) 1, expectedEmployee, expectedAccessGroup, monthAgo);

        expectedEmployee.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

        EmployeeAccessGroup expectedEmployeeAccessGroup2 = new EmployeeAccessGroup((long) 2, expectedEmployee2, expectedAccessGroup, monthFromNow);
        expectedEmployee2.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup2);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee2)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/access/employee/expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName", Matchers.is("Johnny Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].employeeAccessGroupDTOs.[0].expiration", Matchers.is(monthAgo.toString())));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenGetEmployeeBySoonToBeExpired_GivenEmployeeWithSoonToBeExpired_ShouldReturnOnlySoonToBeExpiredEmployeeWithStatus200() throws Exception {

        Employee expectedEmployee = new Employee((long) 1, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        Employee expectedEmployee2 = new Employee((long) 2, "Bug Girl", "burgirl@eworldes.com",
                true, true, new ArrayList<>());

        AccessGroup expectedAccessGroup = new AccessGroup((long) 1, "Test-DHS_FORM", "DHS_FORM", new ArrayList<>());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        LocalDate rightNow = LocalDate.now();
        LocalDate monthAgo = rightNow.minusMonths(1);
        LocalDate monthFromNow = rightNow.plusMonths(1);

        EmployeeAccessGroup expectedEmployeeAccessGroup = new EmployeeAccessGroup((long) 1, expectedEmployee, expectedAccessGroup, monthAgo);

        expectedEmployee.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk());

        EmployeeAccessGroup expectedEmployeeAccessGroup2 = new EmployeeAccessGroup((long) 2, expectedEmployee2, expectedAccessGroup, monthFromNow);
        expectedEmployee2.getEmployeeAccessGroups().add(expectedEmployeeAccessGroup2);

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee2)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/access/employee/soon-expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName", Matchers.is("Bug Girl")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].employeeAccessGroupDTOs.[0].expiration", Matchers.is(monthFromNow.toString())));
    }

    //Puts
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenPutEmployee_GivenNewValuesForEverything_ShouldStatus200() throws Exception {
        Employee expected = new Employee(randomNumber, "Johnny Smith", "johnnysmith@eworldes.com", true, true, new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                        .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        EmployeeDTO employeeDTO = objectMapper.readValue(contentAsString, EmployeeDTO.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName").exists());

        expected.setFullName("Formerly-Johnny Smith");
        expected.setEmail("formerllyjohnnysmith@eworldes.com");
        expected.setBes(false);
        expected.setOffshore(false);

        mockMvc.perform(
                        put("/api/v1/access/employee/" + employeeDTO.getID())
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName").exists());

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenPutEmployee_GivenEmployeeWithEmptyNameAndEmployee_ShouldKeepPreviousValuesWithStatus200() throws Exception {
        Employee expected = new Employee(randomNumber, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        EmployeeDTO employeeDTO = objectMapper.readValue(contentAsString, EmployeeDTO.class);

        expected.setFullName("");
        expected.setEmail("");
        expected.setBes(false);
        expected.setOffshore(false);

        mockMvc.perform(
                        put("/api/v1/access/employee/" + employeeDTO.getID())
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName", Matchers.is("Johnny Smith")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email", Matchers.is("johnnysmith@eworldes.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].offshore", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].bes", Matchers.is(false)));

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenPutEmployee_GivenEmployeeWithSameEmail_ShouldThrowDataIntegrityViolationException() throws Exception {

        Employee expected = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());
        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/employee")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName").exists());

        Employee expected2 = new Employee(randomNumber, "Frank Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataIntegrityViolationException))
                .andExpect(content().string("Duplicate entry."));

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenPutEmployee_GivenEmployeeWithUpdatedEmployeeAccessGroup_ShouldUpdateWithStatus200() throws Exception {

        List<Long> accessGroupIDs1 = Arrays.asList(Long.valueOf(1));
        List<Long> accessGroupIDs2 = Arrays.asList(Long.valueOf(2));


//        Employee expectedEmployee = new Employee((long) 1,"Johnny Smith", "johnnysmith@eworldes.com",
//                true, true, new ArrayList<>());

        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(null, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, accessGroupIDs1, new ArrayList<>());

        AccessGroup expectedAccessGroup = new AccessGroup((long) 1, "Test-DHS_FORM", "DHS_FORM", new ArrayList<>());
        AccessGroup expectedAccessGroup2 = new AccessGroup((long) 2, "Test-AD Group", "AD", new ArrayList<>());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/access/access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedAccessGroup2)))
                .andDo(print())
                .andExpect(status().isOk());

//        LocalDate rightNow = LocalDate.now();
//        rightNow.toString();

        mockMvc.perform(post("/api/v1/access/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployeeDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        expectedEmployeeDTO.setAccessGroupIDs(accessGroupIDs2);

        mockMvc.perform(put("/api/v1/access/employee/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployeeDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeAccessGroupDTOs.[0].accessGroupDTO.name", Matchers.is("Test-AD Group")));
    }


    //Deletes
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenDeleteEmployee_GivenEmployeeID_ShouldDeleteEmployeeWithStatus200() throws Exception {
        Employee expected = new Employee(randomNumber, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        EmployeeDTO employeeDTO = objectMapper.readValue(contentAsString, EmployeeDTO.class);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/access/employee/" + employeeDTO.getID())
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));
    }

//AccessGroup APIs

    //Posts
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void  WhenPostAccessGroup_GivenNormalAccessGroup_ShouldStatus200() throws Exception {

        AccessGroup expected = new AccessGroup("Test-AD", "AD", new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/access_group")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test-AD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("AD")));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void  WhenPostAccessGroup_GivenAccessGroupWithInvalidType_ShouldThrowInvalidAccessGroupTypeException() throws Exception {

        AccessGroup expected = new AccessGroup("Test-AD", "Donkey", new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/access_group")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidAccessGroupTypeException))
                .andExpect(content().string("Invalid Access Group Type. Please input either AD, SECURELINK, OUTLOOK_EMAIL, or DHS_FORM."));

    }

    //Gets
    @Test
    public void  WhenGetAccessGroup_GivenNormalAccessGroup_ShouldStatus200() throws Exception {

        AccessGroup expected = new AccessGroup("Test-AD", "AD", new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/access/access_group")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/access/access_group")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", Matchers.is("Test-AD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].type", Matchers.is("AD")));

    }
    //Puts
    @Test
    public void  WhenPutAccessGroup_GivenUpdatedAccessGroupNameAndType_ShouldUpdateWithStatus200() throws Exception {

        AccessGroup expected = new AccessGroup("Test-AD", "AD", new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        post("/api/v1/access/access_group")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        AccessGroupDTO accessGroupDTO = objectMapper.readValue(contentAsString, AccessGroupDTO.class);

        expected.setName("Updated-Test-AD");
        expected.setType("SECURELINK");

        mockMvc.perform(
                        put("/api/v1/access/access_group/" + accessGroupDTO.getID())
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Updated-Test-AD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("SECURELINK")));

    }

    //Deletes
    @Test
    public void  WhenDeleteAccessGroup_GivenAccessGroupID_ShouldDeleteWithStatus200() throws Exception {

        AccessGroup expected = new AccessGroup("Test-AD", "AD", new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        post("/api/v1/access/access_group")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        AccessGroupDTO accessGroupDTO = objectMapper.readValue(contentAsString, AccessGroupDTO.class);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/access/access_group/" + accessGroupDTO.getID())
                        .content(asJsonString(expected))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));

    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.registerModule(new JavaTimeModule());
//            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
