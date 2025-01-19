package com.employee.managment.employee.dao;

import com.employee.managment.employee.bean.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EmployeeDao extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    List<Employee> findByDepartment(String department);
    List<Employee> findByEmploymentStatus(String employmentStatus);
}
