
package com.example.eworldaccessrequest.integration;

import com.example.eworldaccessrequest.EWorldAccessRequestApplication;
import com.example.eworldaccessrequest.controller.AccessController;
import com.example.eworldaccessrequest.dto.EmployeeDTO;
import com.example.eworldaccessrequest.entity.Employee;
import com.example.eworldaccessrequest.exception.AccessExceptionController;
import com.example.eworldaccessrequest.exception.InvalidAccessGroupTypeException;
import com.example.eworldaccessrequest.service.EmployeeService;
import com.example.eworldaccessrequest.service.AccessGroupService;
import com.example.eworldaccessrequest.service.EmployeeAccessGroupService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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


    Long randomNumber = RandomUtils.nextLong(0, 30);
    Long randomNumber2 = RandomUtils.nextLong(30, 60);


    @Test
    public void  WhenPostEmployee_ShouldStatus200() throws Exception {

        Employee expected = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/access/employee")
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName", Matchers.is("Johnny Smith")));

    }

    @Test
    public void WhenPostDuplicateEmployee_ShouldThrowDataIntegrityViolationException() throws Exception {

        Employee expected = new Employee("Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/access/employee")
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

        Employee expected2 = new Employee(randomNumber, "Johnny Smith", "johnnysmith@eworldes.com",
                true, true, new ArrayList<>());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/access/employee")
                                .content(asJsonString(expected2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DataIntegrityViolationException))
                .andExpect(content().string("Duplicate entry."));

    }




    @Test
    public void WhenUpdateEmployees_ShouldStatus200() throws Exception {
        Employee expected = new Employee(randomNumber, "Johnny Smith", "johnnysmith@eworldes.com", true, true, new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/access/employee")
                                .content(asJsonString(expected))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                        .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        EmployeeDTO employeeDTO = objectMapper.readValue(contentAsString, EmployeeDTO.class);

        System.out.println(employeeDTO);


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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists());

//
//            System.out.println(employeeService.fetchEmployeeList());
//
//            Assert.assertEquals(accessController., mvcResult);



//        MvcResult result = mvc.perform(
//                MockMvcRequestBuilders.get("api/v1/access/employee")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andReturn();
//
//
//        Assert.assertEquals(employeeService.fetchEmployeeList(), result);
//    }
////EMPLOYEE Table
//
//    // Save operation
//    @PostMapping("/access/employee")
//    public EmployeeDTO saveEmployee(@Valid @RequestBody Employee employee) {
//        return employeeService.saveEmployee(employee);
//    }
//
//    // Read operation
//    @GetMapping("/access/employee")
//    public List<EmployeeDTO> fetchEmployeeList() {
//        return employeeService.fetchEmployeeList();
//    }
//
////    @GetMapping("/access/employee/whatever/{id}")
////    public List<EmployeeDTO> fetchTest(@PathVariable("id") Long id) {
////        return employeeService.fetchTest(id);
////    }
//
//    //Read specific employee operation
//    @GetMapping("/access/employee/{email}")
//    public EmployeeDTO findByEmail(@PathVariable("email") String email) {
//        return employeeService.findByEmail(email);
//    }
//
//
//    // Find All Employees With Specific Access Group
//    @GetMapping("/access/employee/accessGroup/{accessGroupID}")
//    public List<EmployeeDTO> findByAccessGroupID(@PathVariable("accessGroupID") Long accessGroupID) {
//        return employeeService.findByAccessGroupID(accessGroupID);
//    }
//
//    // Find All Employees with Expired Access Groups
//    @GetMapping("/access/employee/expired")
//    public List<EmployeeDTO> findEmployeesWithExpiredDHSForms() {
//        return employeeService.findEmployeesWithExpiredDHSForms();
//    }
//
//    // Find All Employees with Soon-to-Be Expired Access Groups
//    @GetMapping("/access/employee/soon-expired")
//    public List<EmployeeDTO> findEmployeesWithSoonToBeExpiredDHSFormsInOneMonth() {
//        return employeeService.findEmployeesWithSoonToBeExpiredDHSFormsInOneMonth();
//    }
//
//    // Update operation
//    @PutMapping("/access/employee/{id}")
//    public EmployeeDTO updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long ID) {
//        return employeeService.updateEmployee(employee, ID);
//    }
//
//    // Delete operation
//    @DeleteMapping("/access/employee/{id}")
//    public String deleteEmployeeById(@PathVariable("id") Long ID) {
//        employeeService.deleteEmployeeById(ID);
//        return "Deleted Successfully";
//    }
//
////ACCESS_GROUP Table
//
//    // Save operation
//    @PostMapping("/access/access_group")
//    public AccessGroupDTO saveAccessGroup(@Valid @RequestBody AccessGroup accessGroup) {
//        return accessGroupService.saveAccessGroup(accessGroup);
//    }
//
//    // Read operation
//    @GetMapping("/access/access_group")
//    public List<AccessGroupDTO> fetchAccessGroupList() {
//        return accessGroupService.fetchAccessGroupList();
//    }
//
//    // Update operation
//    @PutMapping("/access/access_group/{id}")
//    public AccessGroupDTO updateAccessGroup(@RequestBody AccessGroup accessGroup, @PathVariable("id") Long ID) {
//        return accessGroupService.updateAccessGroup(accessGroup, ID);
//    }
//
//    // Delete operation
//    @DeleteMapping("/access/access_group/{id}")
//    public String deleteAccessGroupById(@PathVariable("id") Long ID) {
//        accessGroupService.deleteAccessGroupById(ID);
//        return "Deleted Successfully";
//    }
//
////EMPLOYEE_ACCESS_GROUP Table
//
//    // Save operation
//    @PostMapping("/access/employee_access_group")
//    public EmployeeAccessGroupDTO saveEmployeeAccessGroup(@Valid @RequestBody EmployeeAccessGroup employeeAccessGroup) {
//        return employeeAccessGroupService.saveEmployeeAccessGroup(employeeAccessGroup);
//    }
//
//    // Read operation
//    @GetMapping("/access/employee_access_group")
//    public List<EmployeeAccessGroupDTO> fetchEmployeeAccessGroupList() {
//        return employeeAccessGroupService.fetchEmployeeAccessGroupList();
//    }
//
////    @GetMapping("/access/employee_access_group/{accessGroupID}")
////    public List<EmployeeAccessGroupDTO> findByAccessGroup(@PathVariable("accessGroupID") Long accessGroupID) {
////        return employeeAccessGroupService.findByAccessGroupIDHelper(accessGroupID);
////    }
//
//    // Update operation
//    @PutMapping("/access/employee_access_group/{id}")
//    public EmployeeAccessGroupDTO updateEmployeeAccessGroup(@RequestBody EmployeeAccessGroup employeeAccessGroup, @PathVariable("id") Long ID) {
//        return employeeAccessGroupService.updateEmployeeAccessGroup(employeeAccessGroup, ID);
//    }
//
//    // Delete operation
//    @DeleteMapping("/access/employee_access_group/{id}")
//    public String deleteEmployeeAccessGroupById(@PathVariable("id") Long ID) {
//        employeeAccessGroupService.deleteEmployeeAccessGroupById(ID);
//        return "Deleted Successfully";
//    }
}
