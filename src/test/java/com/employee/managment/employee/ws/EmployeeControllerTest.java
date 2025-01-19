package com.employee.managment.employee.ws;

import com.employee.managment.employee.bean.Employee;
import com.employee.managment.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    public EmployeeControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployees() {
        List<Employee> employees = Collections.singletonList(new Employee());
        when(employeeService.getEmployees()).thenReturn(employees);

        List<Employee> response = employeeController.getEmployees();

        assertEquals(employees, response);
        verify(employeeService, times(1)).getEmployees();
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee();
        when(employeeService.saveEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.createEmployee(employee);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).saveEmployee(employee);
    }
}
