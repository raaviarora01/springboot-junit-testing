package io.github.raaviarora.springboot_junit_testing.controller;

import io.github.raaviarora.springboot_junit_testing.model.Employee;
import io.github.raaviarora.springboot_junit_testing.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/allEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> saveEmployee(@RequestBody Employee emp){
        return employeeService.saveEmployee(emp);
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<String> saveEmployee(@PathVariable Long id, @RequestBody Employee emp){
        return employeeService.updateEmployee(id, emp);
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<String> saveEmployee(@PathVariable Long id){
        return employeeService.deleteEmployee(id);
    }

}
