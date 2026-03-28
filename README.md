# Spring Boot Basic CRUD Template

This project serves as a foundational template for a Spring Boot REST API. It includes basic CRUD operations, relational mapping (One-to-Many / Many-to-One), global exception handling, and Log4j2 logging.

## Entities and Relationships

The application has two primary entities:
- **Department**: Represents a company department (e.g., IT, HR, Finance).
- **Employee**: Represents a company employee. Each employee belongs to one department (Many-to-One relationship).

### Models

#### Department
```json
{
  "id": 1,
  "departmentTitle": "Engineering"
}
```

#### Employee
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "johndoe@example.com",
  "salary": 75000.0,
  "department": {
    "id": 1,
    "departmentTitle": "Engineering"
  }
}
```

## API Endpoints

### Department API (`/api/departments`)

| HTTP Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/departments` | Create a new department | `{"departmentTitle": "HR"}` |
| `GET` | `/api/departments` | Get a list of all departments | None |
| `GET` | `/api/departments/{id}` | Get a single department by its ID | None |
| `PUT` | `/api/departments/{id}` | Update an existing department | `{"departmentTitle": "Human Resources"}` |
| `DELETE` | `/api/departments/{id}` | Delete a department | None |

### Employee API (`/api/employees`)

| HTTP Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/employees` | Create a new employee | `{"name": "Alice", "email": "alice@company.com", "salary": 60000.0, "department": {"id": 1}}` |
| `GET` | `/api/employees` | Get a list of all employees | None |
| `GET` | `/api/employees/{id}` | Get a single employee by their ID | None |
| `PUT` | `/api/employees/{id}` | Update an existing employee | `{"name": "Alice Smith", "email": "alice.s@company.com", "salary": 65000.0, "department": {"id": 2}}` |
| `DELETE` | `/api/employees/{id}` | Delete an employee | None |

## Global Exception Handling

The application uses `@ControllerAdvice` to handle exceptions globally. 
If an entity is not found (e.g., requesting an invalid ID), it throws a `ResourceNotFoundException` which returns a standardized `404 NOT FOUND` response format:

```json
{
    "timestamp": "2023-10-27T10:00:00.000+00:00",
    "message": "Employee not found with id : '99'",
    "details": "uri=/api/employees/99"
}
```

## Logging

Logging is configured using **Log4j2**. Logs are printed to the console and also saved to a file located at `logs/application.log` inside the project root directory.

## Setup Instructions

1. Configure your MySQL database credentials in `src/main/resources/application.properties`.
2. Ensure you have created the target database (e.g., `crud_db`) in MySQL.
3. Run the application. Hibernate will automatically create the required tables `departments` and `employees`.
4. Create a Department first using the POST endpoint.
5. Create Employees and link them to the created Department's ID.