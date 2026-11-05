package com.polarisbank.customer.controller;

import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.entity.Customer;
import com.polarisbank.customer.exception.BusinessException;
import com.polarisbank.customer.service.CustomerService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .thenThrow(new BusinessException("Cliente não encontrado."));

        mockMvc.perform(get("/api/customers/{customerId}", customerId))
                .andExpect(status().isNotFound());
    }
}
