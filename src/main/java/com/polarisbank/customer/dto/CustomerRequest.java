package com.polarisbank.customer.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank (message = "Nome é obrigatório")
        String fullName
) {
  }
