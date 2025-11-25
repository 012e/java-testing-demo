package org.example.javatestingdemo.dtos;

public class BankResponse {
    private final boolean success;
    private final String transactionId;

    public BankResponse(boolean success, String transactionId) {
        this.success = success;
        this.transactionId = transactionId;
    }

    public String getTransactionId() { return transactionId; }
}
