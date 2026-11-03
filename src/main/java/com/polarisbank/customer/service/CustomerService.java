package com.polarisbank.customer.service;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.exception.BusinessException;
import com.polarisbank.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
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

    public Page<Customer> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public void changeCustomerStatus(UUID customerId, StatusCustomer newStatus) {
        if (newStatus == null) {
            throw new BusinessException("O status do cliente é obrigatório.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
        customer.changeStatus(newStatus);
    }
}
