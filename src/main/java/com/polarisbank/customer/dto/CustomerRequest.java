package com.polarisbank.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polarisbank.customer.validation.ValidCpf;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CustomerRequest(
        @NotBlank (message = "Nome é obrigatório")
        String fullName,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(
           regexp = "[0-9]{11}",
           message = "O CPF deve conter exatamente 11 dígitos numéricos"
        )
        @ValidCpf
        String cpf,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail deve ter um formato válido")
        String email,

        @NotNull(message = "A data de nascimento é obrigatória")
        @PastOrPresent(message = "A data de nascimento não pode estar no futuro")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate birthDate
) {
  }
