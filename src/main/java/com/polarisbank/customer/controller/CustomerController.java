package com.polarisbank.customer.controller;

import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.dto.CustomerResponse;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@Valid @RequestBody CustomerRequest request) {
        customerService.createCustomer(request);
    }

    @GetMapping("/{customerId}")
    public CustomerResponse findCustomerById(@PathVariable UUID customerId) {
        Customer customer = customerService.findCustomerById(customerId);

        return new CustomerResponse(
                customer.getIdCustomer(),
                customer.getFullName(),
                customer.getCpf(),
                customer.getEmail(),
                customer.getBirthDate(),
                customer.getStatusCustomer()
        );
    }

    @GetMapping
    public Page<CustomerResponse> findAllCustomers(Pageable pageable) {
        return customerService.findAllCustomers(pageable)
                .map(customer -> new CustomerResponse(
                        customer.getIdCustomer(),
                        customer.getFullName(),
                        customer.getCpf(),
                        customer.getEmail(),
                        customer.getBirthDate(),
                        customer.getStatusCustomer()
                ));
    }

}
