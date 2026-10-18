package com.polarisbank.ledger.entity;

import com.polarisbank.ledger.status.StatusAccount;
import com.polarisbank.ledger.status.StatusBalance;
import com.polarisbank.ledger.status.StatusType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_accounts")
public class LedgerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_ledger_account", nullable = false)
    private UUID idLedgerAccount;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false,length = 20)
    @Enumerated(EnumType.STRING)
    private StatusType type;

    @Column(name = "normal_balance", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private StatusBalance normalBalance;

    @Column(name = "ledger_account_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusAccount ledgerStatusAccount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public LedgerAccount() {
    }

    public UUID getIdLedgerAccount() {
        return idLedgerAccount;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusType getType() {
        return type;
    }

    public void setType(StatusType type) {
        this.type = type;
    }

    public StatusBalance getNormalBalance() {
        return normalBalance;
    }

    public void setNormalBalance(StatusBalance normalBalance) {
        this.normalBalance = normalBalance;
    }

    public StatusAccount getLedgerStatusAccount() {
        return ledgerStatusAccount;
    }

    public void setLedgerStatusAccount(StatusAccount ledgerStatusAccount) {
        this.ledgerStatusAccount = ledgerStatusAccount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
