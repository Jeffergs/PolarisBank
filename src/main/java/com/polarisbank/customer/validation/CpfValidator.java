
package com.polarisbank.customer.validation;

public class CpfValidator {

    public boolean isValid(String cpf) {
        if (cpf == null || !cpf.matches("[0-9]{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int firstDigit = calculateDigit(cpf, 9);
        int secondDigit = calculateDigit(cpf, 10);

        return cpf.charAt(9) - '0' == firstDigit
                && cpf.charAt(10) - '0' == secondDigit;
    }

    private int calculateDigit(String cpf, int length) {
        int sum = 0;

        for (int i = 0; i < length; i++) {
            sum += (cpf.charAt(i) - '0') * (length + 1 - i);
        }

        int remainder = (sum * 10) % 11;
        return remainder == 10 ? 0 : remainder;
    }
}
