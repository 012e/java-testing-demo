package org.example.javatestingdemo.exceptions;

public class BankAPIException extends RuntimeException {
    public BankAPIException(String message) {
        super(message);
    }
}