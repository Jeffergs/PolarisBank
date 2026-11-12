
package com.polarisbank.customer.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseExceptionHandlerTest {

    private final DatabaseExceptionHandler handler = new DatabaseExceptionHandler();

    @Test
    void shouldConvertCpfConstraintViolationToCustomerAlreadyExistsException() {
        DataIntegrityViolationException exception = createException("customers_cpf_key");

        RuntimeException result = handler.handleIntegrityViolation(exception);

        assertInstanceOf(CustomerAlreadyExistsException.class, result);
        assertEquals("CPF já cadastrado.", result.getMessage());
    }

    @Test
    void shouldConvertEmailConstraintViolationToCustomerAlreadyExistsException() {
        DataIntegrityViolationException exception = createException("uk_customers_email_lower");

        RuntimeException result = handler.handleIntegrityViolation(exception);

        assertInstanceOf(CustomerAlreadyExistsException.class, result);
        assertEquals("Email já cadastrado.", result.getMessage());
    }

    @Test
    void shouldReturnOriginalExceptionWhenConstraintIsUnknown() {
        DataIntegrityViolationException exception = createException("unknown_constraint");

        RuntimeException result = handler.handleIntegrityViolation(exception);

        assertSame(exception, result);
    }

    private DataIntegrityViolationException createException(String constraintName) {
        ConstraintViolationException cause = new ConstraintViolationException(
                "Constraint violation",
                new SQLException("Database error"),
                constraintName
        );

        return new DataIntegrityViolationException("Data integrity violation", cause);
    }
}
