package com.polarisbank.customer.dto;

import com.polarisbank.customer.StatusCustomer;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponse(
        UUID idCustomer,
        String fullName,
        String cpf,
        String email,
        LocalDate birthDate,
        StatusCustomer statusCustomer
) {}
