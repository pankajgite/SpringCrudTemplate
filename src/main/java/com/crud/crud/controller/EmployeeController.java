package com.crud.crud.controller;

import com.crud.crud.entity.Employee;
import com.crud.crud.service.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        log.info("REST request to create Employee : {}", employee);
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("REST request to get all Employees");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get Employee : {}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        log.info("REST request to update Employee : {}", id);
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete Employee : {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully.");
    }
}
