package com.polarisbank.customer.dto;

import com.polarisbank.customer.StatusCustomer;
import jakarta.validation.constraints.NotNull;

public record CustomerStatusRequest(
        @NotNull(message = "O status do cliente é obrigatório")
        StatusCustomer statusCustomer
) {
}
