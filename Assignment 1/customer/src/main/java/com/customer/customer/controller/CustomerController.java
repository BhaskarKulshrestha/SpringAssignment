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

    // Create Customer
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    // Get All Customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Get Customer by ID
    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // Update Customer
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

    // Delete Customer
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "Customer deleted with ID: " + id;
    }
}
