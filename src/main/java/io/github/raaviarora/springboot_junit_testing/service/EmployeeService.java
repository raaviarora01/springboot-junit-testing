package io.github.raaviarora.springboot_junit_testing.service;


import io.github.raaviarora.springboot_junit_testing.model.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    ResponseEntity<List<Employee>> getAllEmployees();

    ResponseEntity<Employee> getEmployeeById(Long id);

    ResponseEntity<String> saveEmployee(Employee emp);

    ResponseEntity<String> updateEmployee(Long id, Employee emp);

    ResponseEntity<String> deleteEmployee(Long id);
}
