package com.polarisbank.ledger.entity;

import com.polarisbank.account.entity.Account;
import com.polarisbank.ledger.status.StatusDirection;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_ledger_entry", nullable = false)
    private UUID idLedgerEntry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ledger_account", nullable = false)
    private LedgerAccount ledgerAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ledger_transaction", nullable = false)
    private LedgerTransaction ledgerTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    private Account account;

    @Column(name = "direction", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private StatusDirection direction;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected LedgerEntry() {
    }

    public LedgerEntry(
        LedgerAccount ledgerAccount, LedgerTransaction ledgerTransaction,
        Account account, StatusDirection direction, BigDecimal amount) {
        this.ledgerAccount = ledgerAccount;
        this.ledgerTransaction = ledgerTransaction;
        this.account = account;
        this.direction = direction;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    public UUID getIdLedgerEntry() {
        return idLedgerEntry;
    }

    public LedgerAccount getLedgerAccount() {
        return ledgerAccount;
    }

    public LedgerTransaction getLedgerTransaction() {
        return ledgerTransaction;
    }

    public Account getAccount() {
        return account;
    }

    public StatusDirection getDirection() {
        return direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}