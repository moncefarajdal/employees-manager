# Employee Records Management System

## Overview
The Employee Records Management System is an internal tool designed to centralize the management of employee data across departments. The system provides CRUD operations, role-based permissions, an audit trail, and search and filtering capabilities.

## Features
- **Employee Data Management**: Manage employee attributes such as Full Name, Employee ID, Job Title, Department, etc.
- **Role-Based Permissions**:
  - HR Personnel: Full CRUD operations on employee records.
  - Managers: Limited updates for employees within their department.
  - Administrators: Full system access.
- **Audit Trail**: Logs all changes to employee records.
- **Search and Filter**: Search employees by name, ID, department, or job title; filter by employment status, department, and hire date.
- **Validation Rules**: Ensure valid email formats and unique employee IDs.

## Technology Stack
- **Backend**: Java 17, Spring Boot, Hibernate
- **Database**: Oracle SQL
- **Containerization**: Docker
- **Testing**: JUnit, Mockito, Postman

## Installation

### Prerequisites
- Docker
- Java 17
- Oracle SQL
- Maven

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/moncefarajdal/employees-manager.git
   cd employee-system
2. Configure Database Connection. Edit the `application.properties` file
3. Run the following commands:
   ```bash
   docker-compose build
   docker-compose up
4. The API is documented using Swagger. Once the application is running, access the Swagger UI at:
   http://localhost:8080/swagger-ui.html
5. Credentials for testing the apis:
   ```bash
   ADMIN         : username : admin - password : admin123
   HR            : username : hr - password : hr123
   IT Manager    : username : it_manager - password : manager123
   SALES MANAGER : username : sales_manager - password : manager123
