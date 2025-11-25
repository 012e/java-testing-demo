package org.example.javatestingdemo.dtos;

public class PaymentRequest {
    private final int amount;
    private final String currency;
    private final String customerEmail;

    public PaymentRequest(int amount, String currency, String customerEmail) {
        this.amount = amount;
        this.currency = currency;
        this.customerEmail = customerEmail;
    }

    public int getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCustomerEmail() { return customerEmail; }
}
