package com.polarisbank.customer.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

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

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate parseBirthDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    @Test
    void rejectFullNameBlank() {
        var request = new CustomerRequest("   ", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptFullNameValid() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectFullNameNull() {
        var request = new CustomerRequest(null, "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfNull() {
        var request = new CustomerRequest("Jefferson Gomes", null, "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfBlank() {
        var request = new CustomerRequest("Jefferson Gomes", "   ", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfWithLessThanElevenDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "5299822472", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfWithPunctuation() {
        var request = new CustomerRequest("Jefferson Gomes", "529.982.247-25", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }


    @Test
    void acceptCpfWithValidCheckDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectCpfWithInvalidCheckDigits() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224724", "jefferson@example.com", parseBirthDate("20/05/1995"));
        var violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("CPF inválido", violations.iterator().next().getMessage());
    }

    @Test
    void rejectCpfContainingLetters() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "5299822472A",
                "jefferson@example.com",
                parseBirthDate("20/05/1995")
        );
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectEmailNull() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", null, parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectEmailBlank() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "   ", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectEmailWithInvalidFormat() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "email-invalido", parseBirthDate("20/05/1995"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptEmailValid() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectBirthDateNull() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void rejectBirthDateInFuture() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", LocalDate.now().plusDays(1));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptBirthDateToday() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", LocalDate.now());
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void acceptValidCustomerRequest() {
        var request = new CustomerRequest("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
        assertTrue(validator.validate(request).isEmpty());
    }

}
