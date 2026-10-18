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

    @Column(name = "type", nullable = false,length = 20)
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

    protected LedgerAccount() {
    }

    public LedgerAccount(
        String code, String name, StatusType type,
        StatusBalance normalBalance) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.normalBalance = normalBalance;
        this.ledgerStatusAccount = StatusAccount.ACTIVE;
        this.createdAt = Instant.now();
    }

    public UUID getIdLedgerAccount() {
        return idLedgerAccount;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public StatusType getType() {
        return type;
    }

    public StatusBalance getNormalBalance() {
        return normalBalance;
    }

    public StatusAccount getLedgerStatusAccount() {
        return ledgerStatusAccount;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

}
