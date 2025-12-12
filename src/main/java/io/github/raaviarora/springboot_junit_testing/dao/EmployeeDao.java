package io.github.raaviarora.springboot_junit_testing.dao;

import io.github.raaviarora.springboot_junit_testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}
