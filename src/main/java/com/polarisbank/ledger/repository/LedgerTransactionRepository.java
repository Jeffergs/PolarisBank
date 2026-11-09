package com.polarisbank.ledger.repository;

import com.polarisbank.ledger.entity.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerTransactionRepository extends JpaRepository <LedgerTransaction, UUID> {
}
