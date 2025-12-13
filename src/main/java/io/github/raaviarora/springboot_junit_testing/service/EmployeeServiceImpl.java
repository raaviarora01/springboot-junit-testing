package io.github.raaviarora.springboot_junit_testing.service;

import io.github.raaviarora.springboot_junit_testing.dao.EmployeeDao;
import io.github.raaviarora.springboot_junit_testing.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    EmployeeDao employeeDao;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeDao.findAll();
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(Long id) {
        Optional<Employee> emp = employeeDao.findById(id);

        if(emp.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(emp.get());
    }

    @Override
    public ResponseEntity<String> saveEmployee(Employee emp) {
        try{
            employeeDao.save(emp);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee Added Successfully");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error while adding employee");
        }
    }

    @Override
    public ResponseEntity<String> updateEmployee(Long id, Employee updatedEmp) {

        Optional<Employee> optionalEmp = employeeDao.findById(id);

        if(optionalEmp.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found!!");
        }

        Employee existingEmployee = optionalEmp.get();
        existingEmployee.setFirstName(updatedEmp.getFirstName());
        existingEmployee.setLastName(updatedEmp.getLastName());
        existingEmployee.setEmail(updatedEmp.getEmail());

        try{
            employeeDao.save(existingEmployee);
            return ResponseEntity.ok("Employee Updated Successfully");
        } catch (DataAccessException  e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error while updating employee details");
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployee(Long id) {
        if(!employeeDao.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not found");
        }

        try{
            employeeDao.deleteById(id);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error while deleting employee");
        }
    }
}
