package org.example.javatestingdemo.dtos;

import java.time.LocalDateTime;

public class Transaction {
    private final String transactionId;
    private final int amount;
    private final String currency;
    private final LocalDateTime createdAt;

    public Transaction(String transactionId, int amount, String currency) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = LocalDateTime.now(); // In real app, this would be set by DB
    }

    // Constructor used for mocking save response
    public Transaction(String transactionId) {
        this(transactionId, 0, "MOCK");
    }

    public String getTransactionId() { return transactionId; }
    public int getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}