package com.polarisbank.ledger.entity;

import com.polarisbank.ledger.status.StatusTransaction;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_ledger_transaction", nullable = false)
    private UUID idLedgerTransaction;

    @Column(name = "transaction_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusTransaction transactionType;

    @Column(name = "ledger_transaction_posted", nullable = false)
    private boolean ledgerTransactionPosted;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "posted_at")
    private Instant postedAt;

    protected LedgerTransaction() {
    }

    public LedgerTransaction(
        StatusTransaction transactionType, String description,
        String reference) {
        this.transactionType = transactionType;
        this.ledgerTransactionPosted = false;
        this.description = description;
        this.reference = reference;
        this.createdAt = Instant.now();
    }

    public UUID getIdLedgerTransaction() {
        return idLedgerTransaction;
    }

    public StatusTransaction getTransactionType() {
        return transactionType;
    }

    public boolean isLedgerTransactionPosted() {
        return ledgerTransactionPosted;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPostedAt() {
        return postedAt;
    }

    public void post() {
        if (ledgerTransactionPosted) {
            throw new IllegalStateException( // Depois criar uma exceção personalizada pra isso
                    "A transação já foi contabilizada."
            );
        }

        this.ledgerTransactionPosted = true;
        this.postedAt = Instant.now();
    }
}
