package com.employee.managment.employee.ws;

import com.employee.managment.employee.bean.AuditLog;
import com.employee.managment.employee.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_HR_PERSONNEL', 'ROLE_ADMINISTRATOR') or @securityService.canAccessEmployeeData(#employeeId)")
    @Operation(summary = "Get audit logs for specific employee")
    public ResponseEntity<List<AuditLog>> getEmployeeAuditLogs(@PathVariable Long employeeId) {
        return ResponseEntity.ok(auditLogService.getEmployeeAuditLogs(employeeId));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_HR_PERSONNEL', 'ROLE_ADMINISTRATOR', 'ROLE_MANAGER')")
    @Operation(summary = "Search audit logs with filters")
    public ResponseEntity<Page<AuditLog>> searchAuditLogs(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String changedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.searchAuditLogs(
                employeeId, changedBy, startDate, endDate, pageable));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ROLE_HR_PERSONNEL', 'ROLE_ADMINISTRATOR')")
    @Operation(summary = "Get recent audit logs")
    public ResponseEntity<List<AuditLog>> getRecentChanges(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(auditLogService.getRecentChanges(days));
    }
}
