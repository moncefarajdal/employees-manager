package com.employee.managment.employee.ws;

import com.employee.managment.employee.bean.AuditLog;
import com.employee.managment.employee.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuditLogControllerTest {

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditLogController auditLogController;

    public AuditLogControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeAuditLogs() {
        Long employeeId = 1L;
        List<AuditLog> logs = Collections.singletonList(new AuditLog());
        when(auditLogService.getEmployeeAuditLogs(employeeId)).thenReturn(logs);

        ResponseEntity<List<AuditLog>> response = auditLogController.getEmployeeAuditLogs(employeeId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(logs, response.getBody());
        verify(auditLogService, times(1)).getEmployeeAuditLogs(employeeId);
    }

    @Test
    void testGetRecentChanges() {
        int days = 7;
        List<AuditLog> logs = Collections.singletonList(new AuditLog());
        when(auditLogService.getRecentChanges(days)).thenReturn(logs);

        ResponseEntity<List<AuditLog>> response = auditLogController.getRecentChanges(days);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(logs, response.getBody());
        verify(auditLogService, times(1)).getRecentChanges(days);
    }
}
