# Spring Boot Basic CRUD Template with DTO & Redis Caching

This project serves as a foundational template for a Spring Boot REST API. It includes basic CRUD operations, relational mapping (One-to-Many / Many-to-One), DTO layer mapping, global exception handling, Redis caching, and Log4j2 logging.

## Entities and Relationships

The application has two primary entities:
- **Department**: Represents a company department (e.g., IT, HR, Finance).
- **Employee**: Represents a company employee. Each employee belongs to one department (Many-to-One relationship).

### DTO Models

This project uses the **Data Transfer Object (DTO)** pattern. The API exposes and consumes DTOs instead of the raw database Entities to separate the presentation layer from the database layer, and to allow Redis to properly serialize the data.

#### DepartmentDto
```json
{
  "id": 1,
  "departmentTitle": "Engineering"
}
```

#### EmployeeDto
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

## Redis Caching Implementation

This project implements robust, distributed caching using **Redis**. Caching improves the application's read performance by avoiding unnecessary roundtrips to the database for frequently accessed data.

### Caching Annotations Used

- `@EnableCaching`: Added to the main application class. This acts as a switch to enable Spring's annotation-driven cache management capability. Without this, all other cache annotations are completely ignored.
- `@Cacheable`: Applied to fetch methods (e.g., `getById`). When the method is called, Spring checks the Redis cache first using the generated key.
  - If the key exists, it skips the method body entirely and returns the cached data immediately.
  - If it does not exist, the method executes (hitting the database) and then stores the result in Redis.
- `@CachePut`: Applied to update methods (e.g., `update`). This annotation ensures that the method is ALWAYS executed (so the database is updated), and its result is then placed directly into the Redis cache. This prevents the cache from serving stale, outdated data.
- `@CacheEvict`: Applied to delete methods (e.g., `delete`). This completely removes the specified key from the Redis cache when a record is deleted from the database. This ensures users won't retrieve a deleted entity from the cache.

### Step-by-Step Implementation Guide

**Step 1: Add Dependency**
Add the Spring Data Redis starter to your `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**Step 2: Add Configuration Properties**
In your `src/main/resources/application.properties`, configure the connection to your Redis server:
```properties
spring.redis.host=localhost
spring.redis.port=6379
```

**Step 3: Enable Caching**
Add the `@EnableCaching` annotation to your main Spring Boot application class.

**Step 4: Create Data Transfer Objects (DTOs)**
Because Redis needs to serialize Java objects into byte streams before caching them, passing JPA Entities directly often causes a "Cannot Serialize" error due to Hibernate proxy objects and bidirectional relationships. 
- Create DTOs that implement `java.io.Serializable` (as seen in `com.crud.crud.dto`).
- Use Mappers (`com.crud.crud.mapper`) to convert between Entities (for Database) and DTOs (for Caching and HTTP Responses).

**Step 5: Add Annotations to Service Layer**
Add the caching annotations to your service implementation methods to manage the cache automatically:
```java
@Cacheable(value = "employee", key = "#id")
public EmployeeDto getEmployeeById(Long id) { ... }

@CachePut(value = "employee", key = "#id")
public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) { ... }

@CacheEvict(value = "employee", key = "#id")
public void deleteEmployee(Long id) { ... }
```

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