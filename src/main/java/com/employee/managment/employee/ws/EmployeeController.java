package com.employee.managment.employee.ws;

import com.employee.managment.employee.bean.Employee;
import com.employee.managment.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Employee>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Employee> employees = employeeService.findEmployees(
                name, employeeId, department, jobTitle, status, hireDate, pageable);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_HR_PERSONNEL')")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_HR_PERSONNEL')")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_HR_PERSONNEL')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

