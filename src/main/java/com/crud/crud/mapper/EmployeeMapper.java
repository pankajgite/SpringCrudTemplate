package com.crud.crud.mapper;

import com.crud.crud.dto.EmployeeDto;
import com.crud.crud.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final DepartmentMapper departmentMapper;

    public EmployeeMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setSalary(employee.getSalary());
        dto.setDepartment(departmentMapper.toDto(employee.getDepartment()));
        return dto;
    }

    public Employee toEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setDepartment(departmentMapper.toEntity(dto.getDepartment()));
        return employee;
    }
}
