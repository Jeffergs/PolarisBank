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
        var request = new CustomerRequest("   ", "52998224725");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptFullNameValid() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725");
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectFullNameNull() {
        var request = new CustomerRequest(null, "52998224725");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfNull() {
        var request = new CustomerRequest("Jefferson Gomes", null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfBlank() {
        var request = new CustomerRequest("Jefferson Gomes", "   ");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfWithLessThanElevenDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "5299822472");
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfWithPunctuation() {
        var request = new CustomerRequest("Jefferson Gomes", "529.982.247-25");
        assertFalse(validator.validate(request).isEmpty());
    }


    @Test
    void acceptCpfWithValidCheckDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725");
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfWithInvalidCheckDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224724");
        assertFalse(validator.validate(request).isEmpty());
    }


    @Test
    void rejectCpfWithInvalidCheckDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224724");

        assertFalse(validator.validate(request).isEmpty());
    }

}
