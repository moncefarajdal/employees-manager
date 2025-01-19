package com.employee.managment.employee.utils;

import com.employee.managment.employee.config.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static String getCurrentUserDepartment() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getDepartment();
        }

        throw new RuntimeException("User not properly authenticated");
    }
}
