package com.employee.managment.employee.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee ID is required")
    @Column(unique = true)
    private String employeeId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    @NotBlank(message = "Employment status is required")
    private String employmentStatus;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String address;
}
