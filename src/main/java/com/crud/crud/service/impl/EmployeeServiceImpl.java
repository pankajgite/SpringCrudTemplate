package com.crud.crud.service.impl;

import com.crud.crud.entity.Department;
import com.crud.crud.entity.Employee;
import com.crud.crud.exception.ResourceNotFoundException;
import com.crud.crud.repository.DepartmentRepository;
import com.crud.crud.repository.EmployeeRepository;
import com.crud.crud.service.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Creating a new employee: {}", employee.getName());
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", employee.getDepartment().getId()));
            employee.setDepartment(dept);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        log.info("Updating employee with id: {}", id);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setSalary(employee.getSalary());

        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", employee.getDepartment().getId()));
            existingEmployee.setDepartment(dept);
        }
        
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(existingEmployee);
    }
}
