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


```
Project Dependencies:
Spring Web (To create REST APIs)

Spring Boot DevTools (For hot reloading during development)

Spring Data JPA (For ORM and DB connectivity)

MySQL Driver (To connect with MySQL)

Lombok (To reduce boilerplate code)
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

-----------------------------------------------------

## Swagger Implementation:

Swagger is a suite of open-source tools and a specification (now known as OpenAPI Specification) that helps developers design, document, and interact with RESTful APIs. It facilitates API development by providing tools for designing APIs, generating documentation, and creating client libraries. 

- OpenAPI Specification (OAS):
Swagger's core is the OpenAPI Specification (formerly known as the Swagger Specification), a standard format for describing APIs.
  - Tools:
  - Swagger provides a suite of tools built around the OpenAPI Specification, including:
    - Swagger Editor: A browser-based editor for designing and documenting APIs using the OpenAPI specification.
    - Swagger UI: An interactive tool for visualizing and interacting with APIs documented using the OpenAPI Specification.
    - Swagger Codegen: A tool for generating server stubs and client libraries from an OpenAPI definition. 

- Purpose:
  Swagger helps developers in various stages of API development, including:
    - Design: Designing APIs using a structured format.
    - Documentation: Creating comprehensive and interactive API documentation.
    - Testing: Testing APIs using the interactive documentation provided by Swagger UI.
    - Code Generation: Generating server stubs and client libraries for various programming languages.
    - Collaboration: Facilitating collaboration among developers by providing a common language for describing APIs


# **1️⃣ Swagger Configuration - API Documentation**

## **What is Swagger?**
Swagger (OpenAPI) is a framework that **documents** and **tests** REST APIs interactively.  
It generates a **UI-based API tester** where developers can view and execute API requests.

---

## **🔹 Step 1: Adding Dependencies in `pom.xml`**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

### **What does it do?**
- This dependency **enables Swagger UI** for your project.
- It automatically scans all controllers (`@RestController`) and generates API documentation.
- Provides an interactive web-based **API explorer** at `http://localhost:8080/swagger-ui.html`.

---

## **🔹 Step 2: Creating `SwaggerConfig.java`**

📂 **File Path**: `src/main/java/com/customer/customer/config/SwaggerConfig.java`

```java
package com.customer.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Management API")
                        .description("API for managing customers in a sales unit")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("John Doe")
                                .email("john.doe@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
```

### **What does each part do?**
- `@Configuration` → Tells Spring Boot this is a configuration class.
- `@Bean` → Registers the `OpenAPI` bean in the Spring context.
- `new OpenAPI().info(new Info()...)` → Defines the **metadata** of the API:
    - **Title**: "Customer Management API"
    - **Description**: "API for managing customers in a sales unit"
    - **Version**: "1.0.0"
    - **Contact Info**:
        - Name: "John Doe"
        - Email: "john.doe@example.com"
        - Website: "https://example.com"
    - **License Info**:
        - Name: "Apache 2.0"
        - URL: "https://www.apache.org/licenses/LICENSE-2.0"

---

## **🔹 Step 3: Testing Swagger**

1. Start the Spring Boot application.
2. Open a browser and visit:  
   **`http://localhost:8080/swagger-ui.html`**
3. You'll see an interactive API explorer.

### **Example of API Execution via Swagger:**
- **GET /customers** → Fetch all customers.
- **POST /customers** → Create a new customer.
- **PUT /customers/{id}** → Update an existing customer.
- **DELETE /customers/{id}** → Delete a customer.

---

# **2️⃣ Spring Boot Actuator - Monitoring Health, Info & Metrics**

## **What is Spring Boot Actuator?**
Spring Boot **Actuator** provides built-in endpoints for monitoring and managing the application.  
It includes **health checks**, **application info**, and **performance metrics**.

---

## **🔹 Step 1: Adding Actuator Dependency in `pom.xml`**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### **What does it do?**
- Enables **monitoring** for your application.
- Exposes several RESTful **endpoints** like:
    - `/actuator/health` → Shows application health status.
    - `/actuator/info` → Displays application metadata.
    - `/actuator/metrics` → Shows performance metrics.

---

## **🔹 Step 2: Configuring Actuator in `application.properties`**

📂 **File Path**: `src/main/resources/application.properties`

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
management.endpoint.info.enabled=true
management.info.env.enabled=true
```

### **What does each property do?**
- `management.endpoints.web.exposure.include=health,info,metrics`
    - Exposes the `/actuator/health`, `/actuator/info`, and `/actuator/metrics` endpoints.
- `management.endpoint.health.show-details=always`
    - Displays **detailed health status** instead of just "UP" or "DOWN".
- `management.endpoint.info.enabled=true`
    - Enables the `/actuator/info` endpoint.
- `management.info.env.enabled=true`
    - Includes **environment variables** in the `/actuator/info` endpoint.

---

## **🔹 Step 3: Customizing the `/actuator/info` Endpoint**

📂 **File Path**: `src/main/resources/application.properties`

```properties
info.app.name=Customer Management API
info.app.version=1.0.0
info.app.description=API for managing customer details
```

### **What does it do?**
- Displays custom information in `/actuator/info`:
    - App Name: "Customer Management API"
    - Version: "1.0.0"
    - Description: "API for managing customer details"

---

## **🔹 Step 4: Testing Actuator Endpoints**

Start your app and open:

1. **Health Check**
   ```
   http://localhost:8080/actuator/health
   ```
   **Example Response:**
   ```json
   {
     "status": "UP"
   }
   ```

2. **Application Info**
   ```
   http://localhost:8080/actuator/info
   ```
   **Example Response:**
   ```json
   {
     "app": {
       "name": "Customer Management API",
       "version": "1.0.0",
       "description": "API for managing customer details"
     }
   }
   ```

3. **Application Metrics**
   ```
   http://localhost:8080/actuator/metrics
   ```

---

# **🚀 Summary of Implementations**

| Feature | What it Does | URL |
|---------|-------------|-----|
| **Swagger UI** | Generates API documentation | `http://localhost:8080/swagger-ui.html` |
| **Health Check** | Checks if the application is running | `http://localhost:8080/actuator/health` |
| **App Info** | Shows application metadata | `http://localhost:8080/actuator/info` |
| **Metrics** | Displays performance stats | `http://localhost:8080/actuator/metrics` |

------------------------------------------------

# **Understanding Beans in Spring Boot**

In Spring Boot, **Beans** are objects managed by the **Spring IoC (Inversion of Control) Container**. These are instantiated, assembled, and managed by Spring.

---

## **1️⃣ Bean Annotations in Your Project**
In Spring Boot, Beans are objects managed by the Spring IoC (Inversion of Control) Container. These are instantiated, assembled, and managed by Spring.

How Beans Work in Your Project?
Spring Beans are defined using annotations or configuration classes, and they are used throughout the application for dependency injection.

### **🔹 `@Bean` in SwaggerConfig.java**
```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("Customer Management API")
                    .description("API for managing customers in a sales unit")
                    .version("1.0.0")
                    .contact(new Contact()
                            .name("Your Name")
                            .email("your.email@example.com")
                            .url("https://yourwebsite.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0")));
}
```
### **📌 What is Happening Here?**
- The method **`customOpenAPI()`** is marked with `@Bean`.
- Spring will automatically manage this method and call it when required.
- The returned object (`OpenAPI`) is stored in the **Spring Application Context** as a **Bean**.
- Other components can **reuse** this object instead of creating a new instance.

---

## **2️⃣ Other Common Bean Annotations in Your Project**
### **🔹 `@Component`**
- Makes a class a Spring-managed **Bean**.
- Example:
  ```java
  @Component
  public class MyComponent {
      public void doSomething() {
          System.out.println("Component is working");
      }
  }
  ```
  This can be **autowired** into other classes.

### **🔹 `@Service` (Business Logic Layer)**
```java
@Service
public class CustomerService {
    // Business logic for Customer operations
}
```
- **A specialized `@Component` for service layer.**
- It holds **business logic** and can be autowired.

### **🔹 `@Repository` (Data Layer)**
```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```
- Used in **DAO (Data Access Object) layer**.
- Tells Spring this is a **database interaction class**.
- Uses **Spring Data JPA** to interact with the database.

### **🔹 `@Controller` (API Layer)**
```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
}
```
- **Handles HTTP requests**.
- Works as a **REST API controller**.

---

## **3️⃣ How Beans Interact in Your Project?**
### **Example Flow (GET Customer by ID)**
1. **Controller Calls Service**
   ```java
   @GetMapping("/{id}")
   public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
       return ResponseEntity.ok(customerService.getCustomerById(id));
   }
   ```
2. **Service Calls Repository**
   ```java
   public Customer getCustomerById(Long id) {
       return customerRepository.findById(id).orElseThrow();
   }
   ```
3. **Repository Fetches from Database**
   ```java
   public interface CustomerRepository extends JpaRepository<Customer, Long> { }
   ```

### **Diagram Representation**
```
HTTP Request → Controller (@RestController) → Service (@Service) → Repository (@Repository) → Database
```

---

## **📌 Key Takeaways**
- `@Bean` → Manually define a Bean inside a Configuration class.
- `@Component` → Generic Spring-managed Bean.
- `@Service` → Business logic layer Bean.
- `@Repository` → Data access layer Bean.
- `@Controller` → API request handler.

Would you like me to modify or add anything else in your project? 🚀

