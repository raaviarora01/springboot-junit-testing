package io.github.raaviarora.springboot_junit_testing.service;

import io.github.raaviarora.springboot_junit_testing.dao.EmployeeDao;
import io.github.raaviarora.springboot_junit_testing.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

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
    @DisplayName("Get All Employees")
    public void getAllEmployees_shouldReturnEmployeeList(){
        when(employeeDao.findAll()).thenReturn(Arrays.asList(emp));

        ResponseEntity<List<Employee>> response = employeeService.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(employeeDao, times(1)).findAll();
    }

    @Test
    @DisplayName("Get Employee By Id - FOUND")
    public void getEmployeeById_whenEmployeeExists_shouldReturnEmployee(){
        when(employeeDao.findById(emp.getId())).thenReturn(Optional.of(emp));

        ResponseEntity<Employee> response = employeeService.getEmployeeById(emp.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Raavi", response.getBody().getFirstName());
        verify(employeeDao).findById(emp.getId());
    }

    @Test
    @DisplayName("Get Employee By Id - NOT FOUND")
    public void getEmployeeById_whenEmployeeNotExists_shouldReturn404(){
        when(employeeDao.findById(emp.getId())).thenReturn(Optional.empty());

        ResponseEntity<Employee> response = employeeService.getEmployeeById(emp.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeDao).findById(emp.getId());
    }

    @Test
    @DisplayName("Save Employee - SUCCESS")
    public void saveEmployee_shouldReturnCreated(){
        when(employeeDao.save(any(Employee.class))).thenReturn(emp);

        ResponseEntity<String> response = employeeService.saveEmployee(emp);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Employee Added Successfully", response.getBody());
        verify(employeeDao).save(emp);
    }

    @Test
    @DisplayName("Save Employee - DB Error")
    public void saveEmployee_whenDBError_shouldReturn500(){
        when(employeeDao.save(any(Employee.class))).thenThrow(new DataAccessException("DB Error") {});

        ResponseEntity<String> response = employeeService.saveEmployee(emp);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Database error while adding employee", response.getBody());
    }

    @Test
    @DisplayName("Update Employee - SUCCESS")
    public void updateEmployee_whenEmployeeFound_shouldReturnOk(){
        Employee updatedEmp = Employee.builder()
                .id(1L)
                .firstName("Raavi")
                .lastName("Arora")
                .email("abc@gmail.com")
                .build();

        when(employeeDao.findById(emp.getId())).thenReturn(Optional.ofNullable(emp));
        when(employeeDao.save(any(Employee.class))).thenReturn(emp);

        ResponseEntity<String> response = employeeService.updateEmployee(emp.getId(), updatedEmp);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee Updated Successfully", response.getBody());
        verify(employeeDao, times(1)).findById(emp.getId());
        verify(employeeDao, times(1)).save(emp);
    }

    @Test
    @DisplayName("Update Employee - NOT FOUND")
    public void updateEmployee_whenEmployeeNotFound_shouldReturn404(){
        when(employeeDao.findById(emp.getId())).thenReturn(Optional.empty());

        ResponseEntity<String> response = employeeService.updateEmployee(emp.getId(), emp);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeDao).findById(emp.getId());
        verify(employeeDao, never()).save(any());
    }

    @Test
    @DisplayName("Delete Employee - FOUND")
    public void deleteEmployee_whenEmployeeExists_shouldRDelete(){
        when(employeeDao.existsById(emp.getId())).thenReturn(true);
        doNothing().when(employeeDao).deleteById(emp.getId());

        ResponseEntity<String> response = employeeService.deleteEmployee(emp.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());
        verify(employeeDao).deleteById(emp.getId());
    }

    @Test
    @DisplayName("Delete Employee - NOT FOUND")
    public void deleteEmployee_whenEmployeeNotFound_shouldReturn404(){
        when(employeeDao.existsById(emp.getId())).thenReturn(false);

        ResponseEntity<String> response = employeeService.deleteEmployee(emp.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee Not found", response.getBody());

        verify(employeeDao, times(1)).existsById(emp.getId());
        verify(employeeDao, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete Employee - DB Error")
    public void deleteEmployee_whenDBError_shouldReturn500(){
        when(employeeDao.existsById(emp.getId())).thenReturn(true);
        doThrow(new DataAccessException("DB Error") {}).when(employeeDao).deleteById(emp.getId());

        ResponseEntity<String> response = employeeService.deleteEmployee(emp.getId());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Database error while deleting employee", response.getBody());

        verify(employeeDao, times(1)).existsById(emp.getId());
        verify(employeeDao, times(1)).deleteById(emp.getId());
    }

}
