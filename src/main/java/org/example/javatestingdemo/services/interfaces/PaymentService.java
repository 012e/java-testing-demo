package org.example.javatestingdemo.services.interfaces;

import org.example.javatestingdemo.dtos.PaymentRequest;
import org.example.javatestingdemo.dtos.PaymentResult;
import org.example.javatestingdemo.exceptions.PaymentException;

public interface PaymentService {
    PaymentResult processPayment(PaymentRequest request) throws PaymentException;
}
