
package com.polarisbank.customer.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorTest {

    private final CpfValidator validator = new CpfValidator();

    @Test
    void shouldAcceptCpfWithValidCheckDigits() {
        assertTrue(validator.isValid("52998224725"));
    }

    @Test
    void shouldRejectCpfWithInvalidCheckDigits() {
        assertFalse(validator.isValid("52998224724"));
    }


    @Test
    void shouldRejectNullCpf() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void shouldRejectCpfWithIncorrectLength() {
        assertFalse(validator.isValid("5299822472"));
    }

    @Test
    void shouldRejectCpfWithRepeatedDigits() {
        assertFalse(validator.isValid("11111111111"));
    }

    @Test
    void shouldRejectCpfContainingLetters() {
        assertFalse(validator.isValid("5299822472A"));
    }

    @Test
    void shouldRejectCpfWithMoreThanElevenDigits() {
        assertFalse(validator.isValid("529982247250"));
    }

    @Test
    void shouldRejectCpfContainingSpecialCharacters() {
        assertFalse(validator.isValid("529.982.247-25"));
    }

    @Test
    void shouldRejectBlankCpf() {
        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("   "));
    }

}
