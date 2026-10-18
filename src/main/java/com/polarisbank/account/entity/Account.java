package com.polarisbank.account.entity;

import com.polarisbank.customer.Customer;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_account", nullable = false)
    private UUID idAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(name = "account_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusAccount statusAccount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Account(){
    }

    public Account(Customer customer, String accountNumber) {
        this.customer = customer;
        this.accountNumber = accountNumber;
        this.statusAccount = StatusAccount.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getIdAccount() {
        return idAccount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public StatusAccount getStatusAccount() {
        return statusAccount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
