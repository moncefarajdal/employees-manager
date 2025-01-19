package com.employee.managment.employee.dao;

import com.employee.managment.employee.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role,Long> {
    Role findByUsername(String username);
}
