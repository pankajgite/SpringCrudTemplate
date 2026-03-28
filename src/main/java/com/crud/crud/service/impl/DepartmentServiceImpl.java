package com.crud.crud.service.impl;

import com.crud.crud.entity.Department;
import com.crud.crud.exception.ResourceNotFoundException;
import com.crud.crud.repository.DepartmentRepository;
import com.crud.crud.service.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        log.info("Creating a new department: {}", department.getDepartmentTitle());
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        log.info("Fetching department with id: {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    @Override
    public Department updateDepartment(Long id, Department department) {
        log.info("Updating department with id: {}", id);
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        existingDepartment.setDepartmentTitle(department.getDepartmentTitle());

        return departmentRepository.save(existingDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        departmentRepository.delete(existingDepartment);
    }
}
