package io.github.raaviarora.springboot_junit_testing;

import io.github.raaviarora.springboot_junit_testing.model.Employee;
import io.github.raaviarora.springboot_junit_testing.dao.EmployeeDao;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    @Order(1)
    @Rollback(false)
    public void saveEmployeeTest(){
        Employee emp = Employee.builder()
                .firstName("Raavi")
                .lastName("Arora")
                .email("raavi@gmail.com")
                .build();

        employeeDao.save(emp);

        assertThat(emp.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getEmployeeTest(){
        Employee emp = employeeDao.findById(1L).get();

        assertThat(emp.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void getAllEmployeesTest(){
        List<Employee> employees = employeeDao.findAll();

        assertThat(employees.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(false)
    public void updateEmployeeTest(){
        Employee emp = employeeDao.findById(1L).get();
        emp.setEmail("ram@gmail.com");
        Employee updatedEmployee = employeeDao.save(emp);

        assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
    }

    @Test
    @Order(5)
    @Rollback(false)
    public void deleteEmployeeTest(){
        Employee emp = employeeDao.findById(1L).get();
        employeeDao.delete(emp);

        Employee emp1 = null;
        Optional<Employee> optionalEmployee = employeeDao.findByEmail("ram@gmail.com");

        if(optionalEmployee.isPresent()){
            emp1 = optionalEmployee.get();
        }
        assertThat(emp1).isNull();
    }
}
