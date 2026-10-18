package com.polarisbank.customer;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
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

    @Column(name = "email", nullable = false, unique = true, length = 254)
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
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
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
}
