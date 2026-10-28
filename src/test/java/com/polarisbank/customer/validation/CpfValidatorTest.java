
package com.polarisbank.customer.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorTest {

    @Test
    void acceptCpfWithValidCheckDigits() {
        var validator = new CpfValidator();
        assertTrue(validator.isValid("52998224725"));
    }

    @Test
    void rejectCpfWithInvalidCheckDigits() {
        var validator = new CpfValidator();
        assertFalse(validator.isValid("52998224724"));
    }


    @Test
    void rejectNullCpf() {
        var validator = new CpfValidator();
        assertFalse(validator.isValid(null));
    }

    @Test
    void rejectCpfWithIncorrectLength() {
        var validator = new CpfValidator();
        assertFalse(validator.isValid("5299822472"));
    }

    @Test
    void rejectCpfWithRepeatedDigits() {
        var validator = new CpfValidator();
        assertFalse(validator.isValid("11111111111"));
    }

    @Test
    void rejectCpfContainingLetters() {
        var validator = new CpfValidator();
        assertFalse(validator.isValid("5299822472A"));
    }

}
