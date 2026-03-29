package com.crud.crud.mapper;

import com.crud.crud.dto.DepartmentDto;
import com.crud.crud.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentDto toDto(Department department) {
        if (department == null) {
            return null;
        }
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setDepartmentTitle(department.getDepartmentTitle());
        return dto;
    }

    public Department toEntity(DepartmentDto dto) {
        if (dto == null) {
            return null;
        }
        Department department = new Department();
        department.setId(dto.getId());
        department.setDepartmentTitle(dto.getDepartmentTitle());
        return department;
    }
}
