
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    //Puts
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void WhenUpdateEmployee_GivenNewValuesForEverything_ShouldStatus200() throws Exception {
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
                        MockMvcRequestBuilders.put("/api/v1/access/employee/" + employeeDTO.getID())
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
    public void WhenUpdateEmployee_GivenEmployeeWithEmptyNameAndEmployee_ShouldKeepPreviousValuesWithStatus200() throws Exception {
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
                        MockMvcRequestBuilders.put("/api/v1/access/employee/" + employeeDTO.getID())
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
                        MockMvcRequestBuilders.put("/api/v1/access/access_group/" + accessGroupDTO.getID())
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

//EmployeeAccessGroup APIs

    //Posts
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void  WhenPostEmployeeAccessGroup_GivenNormalEmployeeAndAccessGroup_ShouldStatus200() throws Exception {

        Employee expectedEmployee = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

//        MvcResult resultEmployee = mockMvc.perform(
//                        post("/api/v1/access/employee")
//                                .content(asJsonString(expectedEmployee))
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String contentAsString = resultEmployee.getResponse().getContentAsString();
//        EmployeeDTO employeeDTO = objectMapper.readValue(contentAsString, EmployeeDTO.class);

        AccessGroup expectedAccessGroup = new AccessGroup("Test-DHS_FORM", "DHS_FORM", new ArrayList<>());


//        MvcResult resultAccessGroup = mockMvc.perform(
//                        post("/api/v1/access/access_group")
//                                .content(asJsonString(expectedAccessGroup))
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andReturn();

//        contentAsString = resultAccessGroup.getResponse().getContentAsString();
//        AccessGroupDTO accessGroupDTO = objectMapper.readValue(contentAsString, AccessGroupDTO.class);

//                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test-AD")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is("AD")));

        LocalDate rightNow = LocalDate.now();
        rightNow.toString();

        EmployeeAccessGroup expectedEmployeeAccessGroup = new EmployeeAccessGroup(randomNumber, expectedEmployee, expectedAccessGroup, rightNow);

        mockMvc.perform(post("/api/v1/access/employee_access_group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedEmployeeAccessGroup)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiration", Matchers.is(rightNow.toString())));
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
