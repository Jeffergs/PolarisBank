package com.polarisbank.customer.service;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.exception.CustomerAlreadyExistsException;
import com.polarisbank.customer.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@Transactional
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @PersistenceContext
    private EntityManager entityManager;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("polaris_bank_test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void shouldRejectCustomerRegistrationWhenCpfAlreadyExists() {
        Customer existingCustomer = new Customer(
                "Cliente Existente",
                "52998224725",
                "existente@example.com",
                LocalDate.of(1995, 5, 20)
        );

        customerRepository.saveAndFlush(existingCustomer);

        CustomerRequest request = new CustomerRequest(
                "Novo Cliente",
                "52998224725",
                "novo@example.com",
                LocalDate.of(1990, 8, 10)
        );

        assertThrows(
                CustomerAlreadyExistsException.class,
                () -> customerService.createCustomer(request)
        );
    }

    @Test
    void shouldRejectCustomerRegistrationWhenEmailAlreadyExists() {
        Customer existingCustomer = new Customer(
                "Cliente Existente",
                "98765432100",
                "outro@example.com",
                LocalDate.of(1995, 5, 20)
        );

        customerRepository.saveAndFlush(existingCustomer);

        CustomerRequest request = new CustomerRequest(
                "Novo Cliente",
                "52998224725",
                "OUTRO@example.com",
                LocalDate.of(1990, 8, 10)
        );

        assertThrows(
                CustomerAlreadyExistsException.class,
                () -> customerService.createCustomer(request)
        );
    }

    @Test
    void shouldPersistCustomerStatusChange() {
        Customer customer = new Customer(
                "Cliente Teste",
                "52998224725",
                "cliente@example.com",
                LocalDate.of(1995, 5, 20)
        );

        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        entityManager.clear();

        customerService.changeCustomerStatus(
                savedCustomer.getIdCustomer(),
                StatusCustomer.BLOCKED
        );

        customerRepository.flush();

        Customer updatedCustomer = customerRepository
                .findById(savedCustomer.getIdCustomer())
                .orElseThrow();

        assertEquals(StatusCustomer.BLOCKED, updatedCustomer.getStatusCustomer());
    }
}