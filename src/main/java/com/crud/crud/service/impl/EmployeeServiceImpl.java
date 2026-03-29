package com.crud.crud.service.impl;

import com.crud.crud.dto.EmployeeDto;
import com.crud.crud.entity.Department;
import com.crud.crud.entity.Employee;
import com.crud.crud.exception.ResourceNotFoundException;
import com.crud.crud.mapper.EmployeeMapper;
import com.crud.crud.repository.DepartmentRepository;
import com.crud.crud.repository.EmployeeRepository;
import com.crud.crud.service.EmployeeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating a new employee: {}", employeeDto.getName());
        Employee employee = employeeMapper.toEntity(employeeDto);
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", employee.getDepartment().getId()));
            employee.setDepartment(dept);
        }
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "employee", key = "#id")
    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("Fetching employee with id: {} from Database (Not Cached)", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return employeeMapper.toDto(employee);
    }

    @CachePut(value = "employee", key = "#id")
    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("Updating employee with id: {}", id);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        existingEmployee.setName(employeeDto.getName());
        existingEmployee.setEmail(employeeDto.getEmail());
        existingEmployee.setSalary(employeeDto.getSalary());

        if (employeeDto.getDepartment() != null && employeeDto.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(employeeDto.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", employeeDto.getDepartment().getId()));
            existingEmployee.setDepartment(dept);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @CacheEvict(value = "employee", key = "#id")
    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(existingEmployee);
    }
}
