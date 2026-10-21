package com.polarisbank.customer.repository;

import com.polarisbank.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository <Customer, UUID> {
}
