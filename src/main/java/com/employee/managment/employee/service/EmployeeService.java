package com.employee.managment.employee.service;

import com.employee.managment.employee.bean.Employee;
import com.employee.managment.employee.dao.EmployeeDao;
import com.employee.managment.employee.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AuditLogService auditLogService;

    public List<Employee> getEmployees() {
        return employeeDao.findAll();
    }

    public Page<Employee> findEmployees(String name, String employeeId,
                                        String department, String jobTitle,
                                        String status, LocalDate hireDate, Pageable pageable) {
        Specification<Employee> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%"));
        }
        if (employeeId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("employeeId"), employeeId));
        }
        if (department != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("department"), department));
        }
        if (jobTitle != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("jobTitle"), jobTitle));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("employmentStatus"), status));
        }
        if (hireDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("hireDate"), hireDate));
        }

        return employeeDao.findAll(spec, pageable);
    }


    public Employee saveEmployee(Employee employee) {
        Employee newEmployee = employeeDao.save(employee);
        auditLogService.logChange(newEmployee, "Create User", "Created new employee record");
        return newEmployee;
    }

    public Employee updateEmployee(Long id, Employee newDetails) {
        return employeeDao.findById(id).map(employee -> {
            String changes = compareChanges(employee, newDetails);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR_PERSONNEL")) &&
                    !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"))) {

                if (!employee.getDepartment().equals(getCurrentUserDepartment())) {
                    throw new RuntimeException("Not authorized to modify employees from other departments");
                }
            }

            updateEmployeeDetails(employee, newDetails);
            Employee updatedEmployee = employeeDao.save(employee);
            auditLogService.logChange(updatedEmployee, "Update employee", changes);
            return updatedEmployee;
        }).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    private void updateEmployeeDetails(Employee employee, Employee newDetails) {
        employee.setFullName(newDetails.getFullName());
        employee.setJobTitle(newDetails.getJobTitle());
        employee.setEmploymentStatus(newDetails.getEmploymentStatus());
        employee.setEmail(newDetails.getEmail());
        employee.setPhone(newDetails.getPhone());
        employee.setAddress(newDetails.getAddress());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_HR") ||
                        a.getAuthority().equals("ROLE_ADMIN"))) {
            employee.setDepartment(newDetails.getDepartment());
        }
    }

    public void deleteEmployee(Long id) {
        auditLogService.deleteAllAuditLogsByEmployee(id);
        employeeDao.deleteById(id);
    }

    private String compareChanges(Employee oldEmployee, Employee newEmployee) {
        StringBuilder changes = new StringBuilder();
        if (!oldEmployee.getFullName().equals(newEmployee.getFullName())) {
            changes.append("Full Name changed from ")
                    .append(oldEmployee.getFullName())
                    .append(" to ")
                    .append(newEmployee.getFullName())
                    .append("\n");
        }
        return changes.toString();
    }

    private String getCurrentUserDepartment() {
        String department = SecurityUtils.getCurrentUserDepartment();
        if ("ALL".equals(department)) {
            return null;
        }
        return department;
    }
}
