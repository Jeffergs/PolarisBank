package com.polarisbank.customer.controller;

import com.polarisbank.customer.dto.CustomerRequest;
import com.polarisbank.customer.service.CustomerService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
