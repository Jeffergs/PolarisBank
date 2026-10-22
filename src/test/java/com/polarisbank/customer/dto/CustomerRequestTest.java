package com.polarisbank.customer.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void rejectFullNameBlank() {
        var request = new CustomerRequest("   ");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptFullNameValid() {
        var request = new CustomerRequest("Jefferson Gomes");
        assertTrue(validator.validate(request).isEmpty());
    }

}
