package com.employee.managment.employee.dao;

import com.employee.managment.employee.bean.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogDao extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
    List<AuditLog> findByEmployeeId(Long employeeId);
    List<AuditLog> findByChangedByAndChangedAtBetween(String changedBy, LocalDateTime start, LocalDateTime end);
    List<AuditLog> findByEmployeeIdAndChangedAtBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
}
