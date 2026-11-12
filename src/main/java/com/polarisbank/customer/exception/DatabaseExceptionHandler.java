
package com.polarisbank.customer.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class DatabaseExceptionHandler {

    public RuntimeException handleIntegrityViolation(
            DataIntegrityViolationException exception) {

        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException violation) {
                String constraintName = violation.getConstraintName();

                if (constraintName != null) {
                    switch (constraintName) {
                        case "customers_cpf_key":
                            return new CustomerAlreadyExistsException("CPF já cadastrado.");

                        case "uk_customers_email_lower":
                            return new CustomerAlreadyExistsException("Email já cadastrado.");
                    }
                }
            }

            cause = cause.getCause();
        }

        return exception;
    }
}
