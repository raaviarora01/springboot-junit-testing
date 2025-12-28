package io.github.raaviarora.springboot_junit_testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.raaviarora.springboot_junit_testing.model.Employee;
import io.github.raaviarora.springboot_junit_testing.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee emp;

    @BeforeEach
    void setUp() {
        emp = Employee.builder()
                .id(1L)
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();
    }

    @Test
    @DisplayName("GET /employee/allEmployees - Success")
    public void getAllEmployees_shouldReturnEmployeeList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(ResponseEntity.ok(List.of(emp)));

        mockMvc.perform(get("/employee/allEmployees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Raavi"));
    }

    @Test
    @DisplayName("GET /employee/getEmployee/{id} - Found")
    public void getEmployeeById_shouldReturnEmployee() throws Exception {
        when(employeeService.getEmployeeById(emp.getId())).thenReturn(ResponseEntity.ok(emp));

        mockMvc.perform(get("/employee/getEmployee/{id}", emp.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("raavi@gmail.com"));
    }

    @Test
    @DisplayName("GET /employee/getEmployee/{id} - Not Found")
    public void getEmployeeById_shouldReturn404() throws Exception {
        when(employeeService.getEmployeeById(emp.getId())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(get("/employee/getEmployee/{id}", emp.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /employee/add - Success")
    public void saveEmployee_shouldReturnCreated() throws Exception {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Employee Added Successfully"));

        mockMvc.perform(post("/employee/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee Added Successfully"));
    }

    @Test
    @DisplayName("POST /employee/add - Bad Request")
    public void saveEmployee_whenInvalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(post("/employee/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "firstName": "",
                          "email": "invalid"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /employee/updateEmployee/{id} - Success")
    public void updateEmployee_shouldReturnOk() throws Exception {
        when(employeeService.updateEmployee(eq(1L), any(Employee.class)))
                .thenReturn(ResponseEntity.ok("Employee Updated Successfully"));
        mockMvc.perform(put("/employee/updateEmployee/{id}", emp.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee Updated Successfully"));
    }

    @Test
    @DisplayName("PUT /employee/updateEmployee/{id} - Employee Not Found")
    public void updateEmployee_whenEmployeeNotFound_shouldReturnOk() throws Exception {
        when(employeeService.updateEmployee(eq(1L), any(Employee.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found"));
        mockMvc.perform(put("/employee/updateEmployee/{id}", emp.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee not found"));
    }

    @Test
    @DisplayName("DELETE /employee/deleteEmployee/{id} - Success")
    public void deleteEmployee_shouldReturnOk() throws Exception {
        when(employeeService.deleteEmployee(emp.getId())).thenReturn(ResponseEntity.ok("Employee Deleted Successfully"));

        mockMvc.perform(delete("/employee/deleteEmployee/{id}", emp.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee Deleted Successfully"));
    }

    @Test
    @DisplayName("DELETE /employee/deleteEmployee/{id} - Not Found")
    public void deleteEmployee_whenNotFound_shouldReturn404() throws Exception {
        when(employeeService.deleteEmployee(emp.getId())).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/employee/deleteEmployee/{id}", emp.getId()))
                .andExpect(status().isNotFound());
    }
}
