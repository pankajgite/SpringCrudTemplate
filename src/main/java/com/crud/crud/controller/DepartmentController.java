package com.crud.crud.controller;

import com.crud.crud.entity.Department;
import com.crud.crud.service.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        log.info("REST request to create Department : {}", department);
        return new ResponseEntity<>(departmentService.createDepartment(department), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        log.info("REST request to get all Departments");
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        log.info("REST request to get Department : {}", id);
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        log.info("REST request to update Department : {}", id);
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        log.info("REST request to delete Department : {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully.");
    }
}
