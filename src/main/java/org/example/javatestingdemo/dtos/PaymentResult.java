package org.example.javatestingdemo.dtos;

public class PaymentResult {
    private final String transactionId;
    private final boolean success;

    public PaymentResult(String transactionId, boolean success) {
        this.transactionId = transactionId;
        this.success = success;
    }

    public String getTransactionId() { return transactionId; }
    public boolean isSuccess() { return success; }
}