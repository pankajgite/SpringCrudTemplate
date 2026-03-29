# Spring Boot Redis Caching Guide

This guide explains how to implement robust, distributed caching using **Redis** in a Spring Boot application. Caching improves the application's read performance by avoiding unnecessary roundtrips to the database for frequently accessed data.

## Caching Annotations Used

- `@EnableCaching`: Added to the main application class. This acts as a switch to enable Spring's annotation-driven cache management capability. Without this, all other cache annotations are completely ignored.
- `@Cacheable`: Applied to fetch methods (e.g., `getById`). When the method is called, Spring checks the Redis cache first using the generated key.
  - If the key exists, it skips the method body entirely and returns the cached data immediately.
  - If it does not exist, the method executes (hitting the database) and then stores the result in Redis.
- `@CachePut`: Applied to update methods (e.g., `update`). This annotation ensures that the method is ALWAYS executed (so the database is updated), and its result is then placed directly into the Redis cache. This prevents the cache from serving stale, outdated data.
- `@CacheEvict`: Applied to delete methods (e.g., `delete`). This completely removes the specified key from the Redis cache when a record is deleted from the database. This ensures users won't retrieve a deleted entity from the cache.

## Step-by-Step Implementation Guide

### Step 1: Add Dependency
Add the Spring Data Redis starter to your `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### Step 2: Add Configuration Properties
In your `src/main/resources/application.properties`, configure the connection to your Redis server:
```properties
spring.redis.host=localhost
spring.redis.port=6379
```

### Step 3: Enable Caching
Add the `@EnableCaching` annotation to your main Spring Boot application class:
```java
@SpringBootApplication
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Step 4: Create Data Transfer Objects (DTOs)
Because Redis needs to serialize Java objects into byte streams before caching them, passing JPA Entities directly often causes a "Cannot Serialize" error due to Hibernate proxy objects and bidirectional relationships. 
- Create DTOs that implement `java.io.Serializable`.
- Use Mappers to convert between Entities (for Database) and DTOs (for Caching and HTTP Responses).

### Step 5: Add Annotations to Service Layer
Add the caching annotations to your service implementation methods to manage the cache automatically:

```java
@Cacheable(value = "employee", key = "#id")
public EmployeeDto getEmployeeById(Long id) {
    // ... logic to fetch from database
}

@CachePut(value = "employee", key = "#id")
public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
    // ... logic to update database
}

@CacheEvict(value = "employee", key = "#id")
public void deleteEmployee(Long id) {
    // ... logic to delete from database
}
```

## How to Verify
To verify Redis caching locally: 
1. Trigger your `@Cacheable` endpoint once. Check your application logs to see that a database query was executed.
2. Run `redis-cli` in your terminal and type `KEYS *` to see the generated cache keys (e.g., `employee::1`).
3. Trigger the same endpoint again. You should get a faster response, and your application logs should **not** show a database query this time, confirming the data was retrieved directly from Redis.