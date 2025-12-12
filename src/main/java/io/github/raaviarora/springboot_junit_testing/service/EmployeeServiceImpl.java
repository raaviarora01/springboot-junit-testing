package io.github.raaviarora.springboot_junit_testing.service;

import io.github.raaviarora.springboot_junit_testing.dao.EmployeeDao;
import io.github.raaviarora.springboot_junit_testing.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    EmployeeDao employeeDao;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try{
            return new ResponseEntity<>(employeeDao.findAll(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(Long id) {
        try{
            return new ResponseEntity<>(employeeDao.findById(id).get(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> saveEmployee(Employee emp) {
        employeeDao.save(emp);
        try{
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> updateEmployee(Long id, Employee emp) {

        employeeDao.save(emp);
        try{
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }
}
