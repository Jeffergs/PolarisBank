package com.polarisbank.customer.repository;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.polarisbank.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    private LocalDate parseBirthDate(String date) {
        return LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("polaris_bank_test")
            .withUsername("test")
            .withPassword("test");

    @Test
    void contextLoads() {
    }

    @Test
    void shouldSaveAndFindCustomer() {
        Customer customer = new Customer("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));

        Customer savedCustomer = customerRepository.save(customer);
        Customer foundCustomer = customerRepository.findById(savedCustomer.getIdCustomer()).orElseThrow();

        assertNotNull(foundCustomer.getIdCustomer());
        assertEquals("Jefferson Gomes", foundCustomer.getFullName());
        assertEquals("52998224725", foundCustomer.getCpf());
        assertEquals("jefferson@example.com", foundCustomer.getEmail());
        assertEquals(parseBirthDate("20/05/1995"), foundCustomer.getBirthDate());
        assertEquals(savedCustomer.getIdCustomer(), foundCustomer.getIdCustomer());
    }

    @Test
    void shouldRejectDuplicateCpf() {
        Customer firstCustomer = new Customer("Cliente Um", "52998224725", "cliente1@example.com", parseBirthDate("20/05/1995"));
        Customer secondCustomer = new Customer("Cliente Dois", "52998224725", "cliente2@example.com", parseBirthDate("10/08/1990"));

        customerRepository.saveAndFlush(firstCustomer);

        assertThrows(DataIntegrityViolationException.class, () -> customerRepository.saveAndFlush(secondCustomer));
    }

    @Test
    void shouldReturnTrueWhenCpfExists() {
        Customer customer = new Customer("Cliente Um", "52998224725", "cliente1@example.com", parseBirthDate("20/05/1995"));

        customerRepository.saveAndFlush(customer);

        assertTrue(customerRepository.existsByCpf("52998224725"));
    }

    @Test
    void shouldReturnFalseWhenCpfDoesNotExist() {
        assertFalse(customerRepository.existsByCpf("52998224725"));
    }

    @Test
    void shouldFindAllCustomers() {
        Customer firstCustomer = new Customer("Cliente Um", "52998224725", "cliente1@example.com", parseBirthDate("20/05/1995"));
        Customer secondCustomer = new Customer("Cliente Dois", "98765432100", "cliente2@example.com", parseBirthDate("10/08/1990"));

        customerRepository.save(firstCustomer);
        customerRepository.save(secondCustomer);

        assertEquals(2, customerRepository.findAll().size());
    }

    @Test
    void shouldCountCustomers() {
        Customer firstCustomer = new Customer("Cliente Um", "52998224725", "cliente1@example.com", parseBirthDate("20/05/1995"));
        Customer secondCustomer = new Customer("Cliente Dois", "98765432100", "cliente2@example.com", parseBirthDate("10/08/1990"));

        customerRepository.save(firstCustomer);
        customerRepository.save(secondCustomer);

        assertEquals(2, customerRepository.count());
    }

    @Test
    void shouldReturnTrueWhenCustomerIdExists() {
        Customer customer = new Customer("Cliente Um", "52998224725", "cliente1@example.com", parseBirthDate("20/05/1995"));

        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        assertTrue(customerRepository.existsById(savedCustomer.getIdCustomer()));
    }

    @Test
    void shouldReturnFalseWhenCustomerIdDoesNotExist() {
        assertFalse(customerRepository.existsById(UUID.randomUUID()));
    }





    @Test
    void shouldPersistCustomerStatusChange() {
        Customer customer = new Customer("Cliente Um", "52998224725", "cliente1@example.com", parseBirthDate("20/05/1995"));
        Customer savedCustomer = customerRepository.saveAndFlush(customer);

        savedCustomer.changeStatus(StatusCustomer.INACTIVE);
        customerRepository.saveAndFlush(savedCustomer);

        Customer foundCustomer = customerRepository.findById(savedCustomer.getIdCustomer()).orElseThrow();

        assertEquals(StatusCustomer.INACTIVE, foundCustomer.getStatusCustomer());
    }

    @Test
    void shouldRejectDuplicateEmail() {
        Customer firstCustomer = new Customer("Cliente Um", "52998224725", "cliente@example.com", parseBirthDate("20/05/1995"));
        Customer secondCustomer = new Customer("Cliente Dois", "98765432100", "cliente@example.com", parseBirthDate("10/08/1990"));

        customerRepository.saveAndFlush(firstCustomer);

        assertThrows(DataIntegrityViolationException.class, () -> customerRepository.saveAndFlush(secondCustomer));
    }

    @Test
    void shouldRejectCustomerWithoutFullName() {
        Customer customer = new Customer(null, "52998224725", "cliente@example.com", parseBirthDate("20/05/1995"));
        assertThrows(DataIntegrityViolationException.class, () -> customerRepository.saveAndFlush(customer));
    }

    @Test
    void shouldRejectCustomerWithoutCpf() {
        Customer customer = new Customer("Cliente Um", null, "cliente@example.com", parseBirthDate("20/05/1995"));
        assertThrows(DataIntegrityViolationException.class, () -> customerRepository.saveAndFlush(customer));
    }

    @Test
    void shouldRejectCustomerWithoutEmail() {
        assertThrows(
                BusinessException.class,
                () -> new Customer("Cliente Um", "52998224725", null, parseBirthDate("20/05/1995"))
        );
    }

    @Test
    void shouldRejectCustomerWithoutBirthDate() {
        Customer customer = new Customer("Cliente Um", "52998224725", "cliente@example.com", null);
        assertThrows(DataIntegrityViolationException.class, () -> customerRepository.saveAndFlush(customer));
    }
}