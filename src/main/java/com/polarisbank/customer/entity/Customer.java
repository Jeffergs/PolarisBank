package com.polarisbank.customer.entity;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.exception.BusinessException;
import com.polarisbank.customer.validation.CpfValidator;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_customer", nullable = false)
    private UUID idCustomer;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "customer_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusCustomer statusCustomer;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Customer(){
    }

    public Customer(
        String fullName, String cpf,
        String email, LocalDate birthDate) {
        if (fullName == null || fullName.isBlank()) {
            throw new BusinessException("Nome é obrigatório.");
        }
        if (fullName.length() > 150) {
            throw new BusinessException("O nome deve ter no máximo 150 caracteres.");
        }
        this.fullName = fullName.trim();
        if (!new CpfValidator().isValid(cpf)) {
            throw new BusinessException("CPF inválido.");
        }
        this.cpf = cpf;
        if (email == null || email.isBlank()) {
            throw new BusinessException("E-mail é obrigatório.");
        }
        this.email = email.trim().toLowerCase(Locale.ROOT);
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new BusinessException("A data de nascimento é inválida.");
        }
        this.birthDate = birthDate;
        this.statusCustomer = StatusCustomer.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getIdCustomer() {
        return idCustomer;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public StatusCustomer getStatusCustomer() {
        return statusCustomer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void changeStatus(StatusCustomer statusCustomer) {
        if (statusCustomer == null) {
            throw new BusinessException("O status do cliente é obrigatório.");
        }
        if (this.statusCustomer == statusCustomer) {
            return;
        }
        if (this.statusCustomer == StatusCustomer.BLOCKED) {
            throw new BusinessException(
                    "Não é permitido alterar o status de um cliente bloqueado por esta operação."
            );
        }

        this.statusCustomer = statusCustomer;
        this.updatedAt = Instant.now();
    }
}
