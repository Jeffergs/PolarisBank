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

class CustomerRequestTest {

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
    void shouldRejectFullNameBlank() {
        var request = new CustomerRequest(
                "   ",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptFullNameValid() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectFullNameNull() {
        var request = new CustomerRequest(
                null,
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectCpfNull() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                null,
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectCpfBlank() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "   ",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectCpfWithLessThanElevenDigits() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "5299822472",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectCpfWithPunctuation() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "529.982.247-25",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }


    @Test
    void shouldAcceptCpfWithValidCheckDigits() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectCpfWithInvalidCheckDigits() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224724",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        var violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("CPF inválido", violations.iterator().next().getMessage());
    }

    @Test
    void shouldRejectCpfContainingLetters() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "5299822472A",
                "jefferson@example.com",
                parseBirthDate("20/05/1995")
        );
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectEmailNull() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                null,
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectEmailBlank() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "   ",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectEmailWithInvalidFormat() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "email-invalido",
                parseBirthDate("20/05/1995"));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptEmailValid() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectBirthDateNull() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com", null);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectBirthDateInFuture() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                LocalDate.now().plusDays(1));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptBirthDateToday() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                LocalDate.now());

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptValidCustomerRequest() {
        var request = new CustomerRequest(
                "Jefferson Gomes",
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995"));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptFullNameWith150Characters() {
        String fullName = "A".repeat(150);

        var request = new CustomerRequest(
                fullName,
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995")
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectFullNameWithMoreThan150Characters() {
        String fullName = "A".repeat(151);

        var request = new CustomerRequest(
                fullName,
                "52998224725",
                "jefferson@example.com",
                parseBirthDate("20/05/1995")
        );

        var violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals(
                "O nome deve ter no máximo 150 caracteres",
                violations.iterator().next().getMessage()
        );
    }

}
