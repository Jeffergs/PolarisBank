package com.polarisbank.customer.service;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.exception.CustomerAlreadyExistsException;
import com.polarisbank.customer.exception.CustomerNotFoundException;
import com.polarisbank.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.polarisbank.customer.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
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
        assertThrows(CustomerAlreadyExistsException.class,() -> customerService.createCustomer(request));
        verify(customerRepository).existsByCpf(request.cpf());
        verify(customerRepository, never()).existsByEmailIgnoreCase(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
    }


    @Test
    void shouldSaveCustomerWhenCpfAndEmailDoNotExist() {
        when(customerRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(customerRepository.existsByEmailIgnoreCase(request.email())).thenReturn(false);

        customerService.createCustomer(request);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(captor.capture());

        Customer savedCustomer = captor.getValue();

        assertEquals(request.fullName(), savedCustomer.getFullName());
        assertEquals(request.cpf(), savedCustomer.getCpf());
        assertEquals(request.email(), savedCustomer.getEmail());
        assertEquals(request.birthDate(), savedCustomer.getBirthDate());
        assertEquals(StatusCustomer.ACTIVE, savedCustomer.getStatusCustomer());

        verify(customerRepository).existsByCpf(request.cpf());
        verify(customerRepository).existsByEmailIgnoreCase(request.email());
    }

    @Test
    void shouldRejectCustomerRegistrationWhenEmailAlreadyExists() {
        when(customerRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(customerRepository.existsByEmailIgnoreCase(request.email())).thenReturn(true);

        assertThrows(
                CustomerAlreadyExistsException.class,
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
                CustomerNotFoundException.class,
                () -> customerService.findCustomerById(customerId)
        );

        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldReturnPageOfCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> expectedPage = new PageImpl<>(List.of(customer));

        when(customerRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Customer> result = customerService.findAllCustomers(pageable);

        assertSame(expectedPage, result);
        verify(customerRepository).findAll(pageable);
    }

    @Test
    void shouldChangeCustomerStatusFromActiveToInactive() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        customerService.changeCustomerStatus(customerId, StatusCustomer.INACTIVE);

        assertEquals(StatusCustomer.INACTIVE, customer.getStatusCustomer());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldThrowExceptionWhenChangingStatusOfNonexistentCustomer() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.changeCustomerStatus(customerId, StatusCustomer.INACTIVE));
        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldRejectNullCustomerStatus() {
        UUID customerId = UUID.randomUUID();

        assertThrows(
                BusinessException.class,
                () -> customerService.changeCustomerStatus(customerId, null)
        );

        verifyNoInteractions(customerRepository);
    }

    @Test
    void shouldThrowExceptionWhenChangingStatusOfBlockedCustomer() {
        UUID customerId = UUID.randomUUID();

        customer.changeStatus(StatusCustomer.BLOCKED);

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        assertThrows(
                BusinessException.class,
                () -> customerService.changeCustomerStatus(customerId, StatusCustomer.ACTIVE)
        );

        assertEquals(StatusCustomer.BLOCKED, customer.getStatusCustomer());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldThrowExceptionWhenChangingBlockedCustomerToInactive() {
        UUID customerId = UUID.randomUUID();

        customer.changeStatus(StatusCustomer.BLOCKED);

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        assertThrows(
                BusinessException.class,
                () -> customerService.changeCustomerStatus(customerId, StatusCustomer.INACTIVE)
        );

        assertEquals(StatusCustomer.BLOCKED, customer.getStatusCustomer());
        verify(customerRepository).findById(customerId);
    }

}
