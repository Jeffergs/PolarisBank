package com.polarisbank.customer.controller;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.exception.BusinessException;
import com.polarisbank.customer.exception.CustomerAlreadyExistsException;
import com.polarisbank.customer.exception.CustomerNotFoundException;
import com.polarisbank.customer.service.CustomerService;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void shouldReturnCreatedWhenCustomerIsRegistered() throws Exception {
        String request = """
                {
                    "fullName": "Jefferson Gomes",
                    "cpf": "52998224725",
                    "email": "jefferson@example.com",
                    "birthDate": "20/05/1995"
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        verify(customerService).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void shouldReturnCustomerWhenIdExists() throws Exception {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(
                "Cliente Teste",
                "52998224725",
                "cliente@example.com",
                LocalDate.of(1995,      5, 20)
        );

        ReflectionTestUtils.setField(customer, "idCustomer", customerId);

        when(customerService.findCustomerById(customerId)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCustomer").value(customerId.toString()))
                .andExpect(jsonPath("$.fullName").value("Cliente Teste"))
                .andExpect(jsonPath("$.cpf").value("52998224725"))
                .andExpect(jsonPath("$.email").value("cliente@example.com"))
                .andExpect(jsonPath("$.birthDate").value("1995-05-20"))
                .andExpect(jsonPath("$.statusCustomer").value("ACTIVE"))
                .andExpect(jsonPath("$.createdAt").doesNotExist())
                .andExpect(jsonPath("$.updatedAt").doesNotExist());
    }

    @Test
    void shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
        UUID customerId = UUID.randomUUID();

        when(customerService.findCustomerById(customerId))
                .thenThrow(new CustomerNotFoundException("Cliente não encontrado."));

        mockMvc.perform(get("/api/customers/{customerId}", customerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflictWhenCpfAlreadyExists() throws Exception {
        String request = """
            {
                "fullName": "Cliente Teste",
                "cpf": "52998224725",
                "email": "cliente@example.com",
                "birthDate": "20/05/1995"
            }
            """;

        doThrow(new CustomerAlreadyExistsException("CPF já cadastrado."))
                .when(customerService).createCustomer(any(CustomerRequest.class));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        String request = """
            {
                "fullName": "Cliente Teste",
                "cpf": "52998224725",
                "email": "cliente@example.com",
                "birthDate": "20/05/1995"
            }
            """;

        doThrow(new CustomerAlreadyExistsException("Email já cadastrado."))
                .when(customerService).createCustomer(any(CustomerRequest.class));

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestWhenFullNameIsBlank() throws Exception {
        String request = """
            {
                "fullName": "",
                "cpf": "52998224725",
                "email": "cliente@example.com",
                "birthDate": "20/05/1995"
            }
            """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCpfHasInvalidFormat() throws Exception {
        String request = """
            {
                "fullName": "Cliente Teste",
                "cpf": "123",
                "email": "cliente@example.com",
                "birthDate": "20/05/1995"
            }
            """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        String request = """
            {
                "fullName": "Cliente Teste",
                "cpf": "52998224725",
                "email": "",
                "birthDate": "20/05/1995"
            }
            """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerRequest.class));
    }

    @Test
    void shouldReturnCustomersWithPagination() throws Exception {
        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer(
                "Cliente Teste",
                "52998224725",
                "cliente@example.com",
                LocalDate.of(1995, 5, 20)
        );

        ReflectionTestUtils.setField(customer, "idCustomer", customerId);

        Page<Customer> customerPage = new PageImpl<>(
                List.of(customer),
                PageRequest.of(0, 10),
                1
        );

        when(customerService.findAllCustomers(any(Pageable.class)))
                .thenReturn(customerPage);

        mockMvc.perform(get("/api/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].idCustomer").value(customerId.toString()))
                .andExpect(jsonPath("$.content[0].fullName").value("Cliente Teste"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void shouldReturnEmptyPageWhenNoCustomersExist() throws Exception {
        Page<Customer> emptyPage = Page.empty(PageRequest.of(0, 10));

        when(customerService.findAllCustomers(any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void shouldPassSortingParametersToService() throws Exception {
        when(customerService.findAllCustomers(any(Pageable.class)))
                .thenReturn(Page.empty(PageRequest.of(0, 10, Sort.by("fullName").ascending())));

        mockMvc.perform(get("/api/customers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "fullName,asc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(customerService).findAllCustomers(pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();

        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.Direction.ASC, pageable.getSort().getOrderFor("fullName").getDirection());
    }

    @Test
    void shouldReturnNoContentWhenCustomerStatusIsChanged() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(patch("/api/customers/{customerId}/status", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "statusCustomer": "INACTIVE"
                            }
                            """))
                .andExpect(status().isNoContent());

        verify(customerService).changeCustomerStatus(customerId, StatusCustomer.INACTIVE);
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsNull() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(patch("/api/customers/{customerId}/status", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "statusCustomer": null
                            }
                            """))
                .andExpect(status().isBadRequest());

        verify(customerService, never())
                .changeCustomerStatus(any(UUID.class), any(StatusCustomer.class));
    }

    @Test
    void shouldReturnNotFoundWhenChangingStatusOfNonexistentCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();

        doThrow(new CustomerNotFoundException("Cliente não encontrado."))
                .when(customerService)
                .changeCustomerStatus(customerId, StatusCustomer.INACTIVE);

        mockMvc.perform(patch("/api/customers/{customerId}/status", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "statusCustomer": "INACTIVE"
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNoContentWhenCustomerIsBlocked() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(patch("/api/customers/{customerId}/status", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "statusCustomer": "BLOCKED"
                            }
                            """))
                .andExpect(status().isNoContent());

        verify(customerService).changeCustomerStatus(customerId, StatusCustomer.BLOCKED);
    }

    @Test
    void shouldReturnUnprocessableEntityWhenChangingStatusOfBlockedCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();

        doThrow(new BusinessException("Cliente bloqueado não pode ter o status alterado."))
                .when(customerService)
                .changeCustomerStatus(customerId, StatusCustomer.ACTIVE);

        mockMvc.perform(patch("/api/customers/{customerId}/status", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "statusCustomer": "ACTIVE"
                            }
                            """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Cliente bloqueado não pode ter o status alterado."));
    }

}
