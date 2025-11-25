package org.example.javatestingdemo.exceptions;

public class InvalidPaymentException extends PaymentException {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
