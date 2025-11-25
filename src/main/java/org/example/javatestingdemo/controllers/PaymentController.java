package org.example.javatestingdemo.controllers;

import org.example.javatestingdemo.services.interfaces.BankApiClient;
import org.example.javatestingdemo.services.interfaces.PaymentService;

public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
