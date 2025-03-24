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

    // Default Constructor
    public Customer() {}

    // Parameterized Constructor
    public Customer(String customerName, String address, String phone, String email, Double purchaseValue, Long orderId) {
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.purchaseValue = purchaseValue;
        this.orderId = orderId;
    }

    // Getters and Setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getPurchaseValue() { return purchaseValue; }
    public void setPurchaseValue(Double purchaseValue) { this.purchaseValue = purchaseValue; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
