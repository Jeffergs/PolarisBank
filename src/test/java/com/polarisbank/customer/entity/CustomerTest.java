package com.polarisbank.customer.entity;

import com.polarisbank.customer.StatusCustomer;
import com.polarisbank.customer.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate parseBirthDate(String date) {
            return LocalDate.parse(date, DATE_FORMATTER);
        }

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995")
        );
    }

    @Test
    void shouldCreateCustomerWithActiveStatus() {
        assertEquals(StatusCustomer.ACTIVE, customer.getStatusCustomer());
    }

    @Test
    void shouldCreateCustomerWithInitializedAuditTimestamps() {
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
        assertEquals(customer.getCreatedAt(), customer.getUpdatedAt());
    }

    @Test
    void shouldCreateCustomerWithProvidedData() {
        var birthDate = customer.getBirthDate();
        assertEquals("Jefferson Gomes", customer.getFullName());
        assertEquals("52998224725", customer.getCpf());
        assertEquals("jefferson@example.com", customer.getEmail());
        assertEquals(parseBirthDate("20/05/1995"), customer.getBirthDate());
    }

    @Test
    void shouldChangeCustomerStatus() {
        customer.changeStatus(StatusCustomer.INACTIVE);
        assertEquals(StatusCustomer.INACTIVE, customer.getStatusCustomer());
        assertNotNull(customer.getUpdatedAt());
    }

    @Test
    void shouldUpdateTimestampWhenCustomerStatusChanges() throws InterruptedException {
        Instant originalUpdatedAt = customer.getUpdatedAt();
        Thread.sleep(2);
        customer.changeStatus(StatusCustomer.INACTIVE);
        assertTrue(customer.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void shouldRejectStatusChangeWhenCustomerIsBlocked() {
        customer.changeStatus(StatusCustomer.BLOCKED);
        assertThrows(
            BusinessException.class,
            () -> customer.changeStatus(StatusCustomer.ACTIVE)
        );
    }

    @Test
    void shouldNotUpdateTimestampWhenStatusRemainsTheSame() {
        Instant originalUpdatedAt = customer.getUpdatedAt();
        customer.changeStatus(StatusCustomer.ACTIVE);
        assertEquals(StatusCustomer.ACTIVE, customer.getStatusCustomer());
        assertEquals(originalUpdatedAt, customer.getUpdatedAt());
    }

    @Test
    void shouldRejectCustomerWithNullName() {
        assertThrows(
            BusinessException.class,
            () -> new Customer(
               null,
               "52998224725",
               "jefferson@example.com",
               parseBirthDate("20/05/1995")
               )
        );
    }

    @Test
    void shouldRejectCustomerWithBlankName() {
        assertThrows(
            BusinessException.class,
            () -> new Customer(
                "   ",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995")
            )
        );
    }

    @Test
    void shouldRejectCustomerWithInvalidCpf() {
        assertThrows(
             BusinessException.class,
             () -> new Customer(
                  "Jefferson Gomes",
                  "52998224724",
                  "jefferson@example.com",
                  parseBirthDate("20/05/1995")
             )
        );
    }

    @Test
    void shouldRejectCustomerWithFutureBirthDate() {
        assertThrows(
            BusinessException.class,
            () -> new Customer(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                LocalDate.now().plusDays(1)
            )
        );
    }

    @Test
    void shouldRejectNullStatus() {
        assertThrows(
             BusinessException.class,
             () -> customer.changeStatus(null)
        );
    }

    @Test
    void shouldRejectCustomerWithoutEmail() {
        assertThrows(
            BusinessException.class,
            () -> new Customer(
                "Cliente Um",
                "52998224725",
                null,
                parseBirthDate("20/05/1995"))
        );
    }

    @Test
    void shouldRejectCustomerWithBlankEmail() {
        assertThrows(
             BusinessException.class,
             () -> new Customer(
                 "Cliente Um",
                 "52998224725",
                 "   ",
                 parseBirthDate("20/05/1995")
             )
        );
    }

    @Test
    void shouldThrowExceptionWhenFullNameExceeds150Characters() {
        String fullName = "A".repeat(151);

            assertThrows(
                BusinessException.class,
                () -> new Customer(
                     fullName,
                     "52998224725",
                     "cliente@example.com",
                     LocalDate.of(1995, 5, 20)
                )
        );
    }
}

