package com.polarisbank.customer.service;

import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.exception.BusinessException;
import com.polarisbank.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(CustomerRequest request) {
        if (customerRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado.");
        }
        if (customerRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Email já cadastrado.");
        }
        Customer customer = new Customer(
                request.fullName(),
                request.cpf(),
                request.email(),
                request.birthDate()
        );
        customerRepository.save(customer);
    }

    public Customer findCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
    }
}
