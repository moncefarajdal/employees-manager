package com.employee.managment.employee.service;

import com.employee.managment.employee.bean.AuditLog;
import com.employee.managment.employee.bean.Employee;
import com.employee.managment.employee.dao.AuditLogDao;
import com.employee.managment.employee.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogDao auditLogDao;

    @Transactional
    public void logChange(Employee employee, String action, String changes) {
        AuditLog log = new AuditLog();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.setEmployee(employee);
        log.setChangedBy(auth.getName());
        log.setChangedAt(LocalDateTime.now());
        log.setChanges(formatChangeLog(action, changes));
        auditLogDao.save(log);
    }

    private String formatChangeLog(String action, String changes) {
        return String.format("Action: %s\nChanges:\n%s", action, changes);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getEmployeeAuditLogs(Long employeeId) {
        return auditLogDao.findByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public Page<AuditLog> searchAuditLogs(
            Long employeeId,
            String changedBy,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        Specification<AuditLog> spec = Specification.where(null);

        if (employeeId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("employee").get("id"), employeeId));
        }

        if (changedBy != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("changedBy"), changedBy));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("changedAt"), startDate, endDate));
        }

        String userDepartment = SecurityUtils.getCurrentUserDepartment();
        if (!"ALL".equals(userDepartment)) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("employee").get("department"), userDepartment));
        }

        return auditLogDao.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getRecentChanges(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return auditLogDao.findAll((root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("changedAt"), startDate));
    }

    @Transactional
    public void deleteAllAuditLogsByEmployee(Long employeeId) {
        List<AuditLog> logs = auditLogDao.findByEmployeeId(employeeId);
        auditLogDao.deleteAll(logs);
    }
}
