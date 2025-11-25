package org.example.javatestingdemo.services.interfaces;

import org.example.javatestingdemo.dtos.BankResponse;
import org.example.javatestingdemo.dtos.PaymentRequest;
import org.example.javatestingdemo.exceptions.BankAPIException;

public interface BankApiClient {
    BankResponse charge(PaymentRequest request) throws BankAPIException;
}
