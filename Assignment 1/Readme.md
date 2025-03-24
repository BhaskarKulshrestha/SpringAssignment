```customer/
│── src/main/java/com/customer/customer/
│   ├── CustomerApplication.java  → (Main entry point)
│   ├── model/
│   │   ├── Customer.java  → (Entity class for database)
│   ├── repository/
│   │   ├── CustomerRepository.java  → (Database interactions)
│   ├── service/
│   │   ├── CustomerService.java  → (Business logic)
│   ├── controller/
│   │   ├── CustomerController.java  → (Handles API requests)
│── src/main/resources/
│   ├── application.properties  → (Configuration settings)
│── pom.xml  → (Maven dependencies)
```

```
com
└── customer
    ├── model          # Contains entity classes (Customer.java)
    ├── repository     # Contains repository interfaces (CustomerRepository.java)
    ├── service        # Contains business logic (CustomerService.java)
    ├── controller     # Contains API endpoints (CustomerController.java)

```

## How Spring Boot Works in This Project
Spring Boot follows a layered architecture:

Controller Layer → Handles HTTP requests (CustomerController).
Service Layer → Handles business logic (CustomerService).
Repository Layer → Handles database operations (CustomerRepository).
Model Layer → Represents database structure (Customer).

## Understanding Each File with Code and Examples
Step 1: Customer.java (Entity Layer - Represents Database Table)
Location: src/main/java/com/customer/customer/model/Customer.java

What It Does
Defines the Customer table.
Uses JPA annotations (@Entity, @Table).
Uses @Id and @GeneratedValue to auto-generate primary key.

```java
package com.customer.customer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String customerName;
    private String address;
    private String phone;
    private String email;
    private Double purchaseValue;
    private Long orderId;

    // Getters and Setters (Omitted for brevity)
}
```
it will be stored like this:
```
customerId | customerName | address  | phone      | email          | purchaseValue | orderId
-----------|-------------|----------|------------|---------------|--------------|--------
1          | John Doe    | NYC      | 9876543210 | john@example.com | 500.75       | 101
```

## Step 2: CustomerRepository.java (Database Access)
Location: src/main/java/com/customer/customer/repository/CustomerRepository.java

What It Does
Extends JpaRepository<Customer, Long>, which provides built-in database operations like:
findAll() → Get all customers.
findById(id) → Get a customer by ID.
save(customer) → Insert or update customer.
deleteById(id) → Delete customer by ID.

```java
package com.customer.customer.repository;

import com.customer.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```

- public interface CustomerRepository
  - Defines an interface (not a class) for interacting with the database.
  - Spring automatically provides the implementation (we don't need to write SQL queries manually).
- extends JpaRepository<Customer, Long>
  - Inherits from JpaRepository, which provides built-in CRUD operations.
    - <Customer, Long> means:
      - Customer → The entity that this repository will manage.
      - Long → The type of the primary key (id field in Customer).

**Explanation:**
```
import com.customer.customer.model.Customer;
```
Imports the Customer entity from the model package.
This is necessary to tell the repository which entity it should handle.

```java
import org.springframework.data.jpa.repository.JpaRepository;
```
Imports the JpaRepository interface from Spring Data JPA.
This is the key part that allows the repository to interact with the database.

#### public interface CustomerRepository

 - Defines an interface (not a class) for interacting with the database. 
 - Spring automatically provides the implementation (we don't need to write SQL queries manually).

#### extends JpaRepository<Customer, Long>

 - Inherits from JpaRepository, which provides built-in CRUD operations.
   - `<Customer, Long>`  means:
     - Customer → The entity that this repository will manage.
     - Long → The type of the primary key (id field in Customer).

```java
import com.customer.customer.model.Customer;
```
- Imports the Customer entity from the model package.
- This is necessary to tell the repository which entity it should handle.
```java
import org.springframework.data.jpa.repository.JpaRepository;
```
- Imports the JpaRepository interface from Spring Data JPA.
- This is the key part that allows the repository to interact with the database.

How It Works Internally

1. Spring Boot Auto-Configuration
   - Spring Boot detects CustomerRepository as a Spring Data JPA repository. 
   - It automatically creates an implementation at runtime.
2. Built-in Methods from JpaRepository Without writing any code, you get methods like:

    - save(Customer customer) → Inserts or updates a record.
    - findById(Long id) → Finds a customer by its ID.
    - findAll() → Fetches all customers.
    - deleteById(Long id) → Deletes a customer by ID.

## Step 3: CustomerService.java (Business Logic Layer)
Location: src/main/java/com/customer/customer/service/CustomerService.java

What It Does
- Calls CustomerRepository to perform CRUD operations.
- Uses @Service to tell Spring this is a business logic class.

```java
package com.customer.customer.service;

import com.customer.customer.model.Customer;
import com.customer.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
```
## Step 4: CustomerController.java (Handling API Requests)
Location: src/main/java/com/customer/customer/controller/CustomerController.java

What It Does
- Uses @RestController → Converts Java objects into JSON.
- Uses @RequestMapping("/customers") → Defines API endpoints.
- Calls CustomerService to perform actions.

```java
package com.customer.customer.controller;

import com.customer.customer.model.Customer;
import com.customer.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(id);

        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setCustomerName(customerDetails.getCustomerName());
            existingCustomer.setAddress(customerDetails.getAddress());
            existingCustomer.setPhone(customerDetails.getPhone());
            existingCustomer.setEmail(customerDetails.getEmail());
            existingCustomer.setPurchaseValue(customerDetails.getPurchaseValue());
            existingCustomer.setOrderId(customerDetails.getOrderId());

            return customerService.saveCustomer(existingCustomer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "Customer deleted with ID: " + id;
    }
}
```

`To start a mvn application:`
```java
mvn spring-boot:run
```

----------------------------------------------------------------

### What is the maven-wrapper in springboot 

The Maven Wrapper (mvnw and mvnw.cmd) in a Spring Boot project is a tool that ensures a specific version of Maven is used, even if it's not installed on the system. It simplifies project setup and makes builds more consistent across different environments.

Why Use Maven Wrapper?
Ensures a Specific Maven Version – Prevents issues due to different Maven versions on different systems.

No Need to Install Maven – Developers can run Maven commands using the wrapper without manually installing Maven.

Improves CI/CD Consistency – Helps maintain build consistency in CI/CD pipelines.

Files in Maven Wrapper
When you generate a Spring Boot project with the Maven Wrapper enabled, you get:

    mvnw (Unix/Linux/Mac script)
    mvnw.cmd (Windows batch script)
    maven-wrapper.jar (Used to download the correct Maven version)
    maven-wrapper.properties (Defines the Maven version to use)

----------------------------------------------------------------

### What is @Autowired in Spring Boot?

@Autowired is a Spring annotation used for automatic dependency injection. It allows Spring to inject a required bean (class object) automatically without explicitly instantiating it using new.

Why Use @Autowired?
    - Removes manual object creation (new keyword).
    - Simplifies Dependency Injection (DI).
    - Promotes loose coupling between classes.

How Does @Autowired Work?
- Spring manages a container of Beans (objects) and automatically injects dependencies where needed.

```java
@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository; // Injected automatically

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
```

- Spring finds a matching bean (CustomerRepository).
- Injects it automatically into customerRepository (without new).

----------------------------------------------------------------

### What is @RestController and @RequestMapping("/customers") in Spring Boot?

- @RestController
    - @RestController is a Spring annotation that combines:
    - @Controller → Marks a class as a Spring MVC controller.
    - @ResponseBody → Converts Java objects to JSON automatically.
```java
@RestController
public class CustomerController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
```
- Without @RestController, we would need to use @ResponseBody manually.
- The response is automatically converted to JSON.

*Equivalent Code Without @RestController*
```java
@Controller
public class CustomerController {
    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello, World!";
    }
}
```

- @RequestMapping("/customers")
    - @RequestMapping is used to map a URL path to a controller or method.
```java
      @RestController
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping
    public String getCustomers() {
        return "List of Customers";
    }
}
```
- The base URL for all endpoints in this controller is /customers.
- The @GetMapping maps GET /customers to the getCustomers() method.

*Equivalent Without @RequestMapping*
```java
@RestController
public class CustomerController {
    @GetMapping("/customers")
    public String getCustomers() {
        return "List of Customers";
    }
}
```

------------------------------------------------

### How does the unique ID is generated ?
In your Spring Boot REST API, the customerId is automatically generated as a unique primary key when a new customer is saved in the database. This is achieved using `JPA` (Java Persistence API) and `Hibernate`, which handle auto-incrementing the ID.

### What is JPA ?

JPA, or Java Persistence API, is a Java specification that provides a standard way to manage relational data in Java applications, acting as a bridge between object-oriented domain models and relational database systems.

- What is does ?

    - Object-Relational Mapping (ORM): JPA facilitates ORM, which is the process of mapping Java objects to database tables and vice-versa.
    - Data Persistence: It provides a way to persist Java objects in a database.
    - Standardization: JPA defines a standardized approach to working with databases, making it easier to write database-independent code.
    - Simplification: It simplifies database operations by allowing developers to interact with databases using Java objects instead of writing SQL queries directly. 
- key Concept
  - Entities: Java objects that are mapped to database tables.
  - Entity Manager: An API that manages the persistence of entities.
  - Persistence Provider: An implementation of the JPA specification that provides the actual persistence logic.
  - JPQL (Java Persistence Query Language): A query language for retrieving objects from the database. 
- It is used in the Jakarta EE = > jakarta enterprise edition ==> akarta EE (formerly Java EE and Java 2 Platform, Enterprise Edition or J2EE) is a set of specifications and APIs that extend the Java Standard Edition (Java SE) with features for building enterprise applications, focusing on technologies like distributed computing and web services. 



### What is hibernate ?

Hibernate is an open-source Object-Relational Mapping (ORM) framework for Java that simplifies database interactions by mapping Java objects to database tables, allowing developers to work with data using objects instead of writing SQL queries directly. 

- Object-Relational Mapping (ORM):
Hibernate acts as a bridge between the object-oriented world of Java and the relational world of databases. 
- Simplified Database Interactions:
  It provides a high-level API for interacting with relational databases, reducing the need for developers to write complex SQL queries. 
- Mapping Java Objects to Database Tables:
  Hibernate maps Java classes to database tables and Java data types to SQL data types, allowing you to work with objects instead of raw SQL. 

- Benefits of using Hibernate:
  - Reduced boilerplate code: Hibernate handles the mapping and persistence logic, reducing the amount of code developers need to write.
  - Improved productivity: Developers can focus on the application logic rather than database-specific details.
  - Database independence: Hibernate can work with different relational databases, making your application more portable.
  - Automatic transaction management and caching: Hibernate provides features like automatic transaction management and caching, which can improve performance and simplify development. 
  
- Hibernate and JPA:
  Hibernate is an implementation of the Java Persistence API (JPA), a standard for persistence in Java applications. 
- Hibernate is an open-source framework




