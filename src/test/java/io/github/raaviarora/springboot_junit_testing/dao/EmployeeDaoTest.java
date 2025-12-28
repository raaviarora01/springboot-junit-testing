package io.github.raaviarora.springboot_junit_testing.dao;

import io.github.raaviarora.springboot_junit_testing.model.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    @DisplayName("Save employee into database")
    public void EmployeeDao_SaveEmployee_PersistsEmployee(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        Employee savedEmp = employeeDao.save(emp);

        assertThat(savedEmp).isNotNull();
        assertThat(savedEmp.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Find employee by Id")
    public void EmployeeDao_FindById_ReturnEmployee(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        Employee savedEmp = employeeDao.save(emp);

        Optional<Employee> foundEmp = employeeDao.findById(savedEmp.getId());

        assertThat(foundEmp).isPresent();
        assertThat(foundEmp.get().getFirstName()).isEqualTo("Raavi");
    }

    @Test
    @DisplayName("Find employee by Email")
    public void EmployeeDao_FindByEmail_ReturnEmployeeNotNull(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        Optional<Employee> foundEmp = employeeDao.findByEmail(emp.getEmail());

        assertThat(foundEmp).isNotNull();
        assertThat(foundEmp.get().getFirstName()).isEqualTo("Raavi");
    }

    @Test
    @DisplayName("Find all employees")
    public void EmployeeDao_FindAllEmployees_ReturnsMoreThanOneEmployee(){
        Employee emp1 = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        Employee emp2 = Employee.builder()
                .firstName("Pulkit")
                .lastName("Sharma")
                .email("pulkit@gmail.com")
                .build();

        employeeDao.save(emp1);
        employeeDao.save(emp2);

        List<Employee> empList = employeeDao.findAll();

        assertThat(empList).isNotNull();
        assertThat(empList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Update employee details in database")
    public void EmployeeDao_UpdateEmployee_ReturnsNotNullEmployee(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        employeeDao.save(emp);

        Employee savedEmp = employeeDao.findById(emp.getId()).get();
        savedEmp.setEmail("arora@gmail.com");

        Employee updatedEmp = employeeDao.save(savedEmp);

        assertThat(updatedEmp.getEmail()).isNotNull();
        assertThat(updatedEmp.getEmail()).isEqualTo("arora@gmail.com");
    }

    @Test
    @DisplayName("Delete Employee by Id")
    public void EmployeeDao_DeleteById_ReturnsEmployeeIsNull(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        Employee savedEmp = employeeDao.save(emp);
        employeeDao.deleteById(savedEmp.getId());

        Optional<Employee> deletedEmp = employeeDao.findById(emp.getId());

        assertThat(deletedEmp).isEmpty();
    }

    @Test
    @DisplayName("Delete Employee by Email")
    public void EmployeeDao_DeleteByEmail_ReturnsEmployeeIsNull(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        Employee savedEmp = employeeDao.save(emp);
        employeeDao.deleteByEmail(savedEmp.getEmail());

        Optional<Employee> deletedEmp = employeeDao.findByEmail(emp.getEmail());

        assertThat(deletedEmp).isEmpty();
    }
}
