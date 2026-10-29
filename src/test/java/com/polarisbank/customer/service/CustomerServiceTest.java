package com.polarisbank.customer.service;

import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.polarisbank.customer.exception.BusinessException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;
    private CustomerRequest request;
    private Customer customer;

    @BeforeEach
    void setUp() {
        request = new CustomerRequest(
                "João Silva",
                "52998224725",
                "joao@email.com",
                LocalDate.of(1995, 5, 20)
        );

        customer = new Customer(
                "João Silva",
                "52998224725",
                "joao@email.com",
                LocalDate.of(1995, 5, 20)
        );
    }

    @Test
    void shouldRejectCustomerRegistrationWhenCpfAlreadyExists() {
        when(customerRepository.existsByCpf(request.cpf())).thenReturn(true);
        assertThrows(BusinessException.class,() -> customerService.createCustomer(request));
        verify(customerRepository, never()).existsByEmailIgnoreCase(anyString());
        verify(customerRepository).existsByCpf(request.cpf());
        verify(customerRepository, never()).save(any(Customer.class));
    }


    @Test
    void shouldSaveCustomerWhenCpfAndEmailDoNotExist() {
        when(customerRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(customerRepository.existsByEmailIgnoreCase(request.email())).thenReturn(false);

        customerService.createCustomer(request);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(captor.capture());

        Customer customer = captor.getValue();

        assertEquals(request.fullName(), customer.getFullName());
        assertEquals(request.cpf(), customer.getCpf());
        assertEquals(request.email(), customer.getEmail());
        assertEquals(request.birthDate(), customer.getBirthDate());
    }

    @Test
    void shouldRejectCustomerRegistrationWhenEmailAlreadyExists() {
        when(customerRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(customerRepository.existsByEmailIgnoreCase(request.email())).thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> customerService.createCustomer(request)
        );

        verify(customerRepository).existsByCpf(request.cpf());
        verify(customerRepository).existsByEmailIgnoreCase(request.email());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldReturnCustomerWhenIdExists() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.findCustomerById(customerId);

        assertSame(customer, result);
        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> customerService.findCustomerById(customerId)
        );

        verify(customerRepository).findById(customerId);
    }

}
