package org.example.javatestingdemo.services;

import org.example.javatestingdemo.dtos.BankResponse;
import org.example.javatestingdemo.dtos.PaymentRequest;
import org.example.javatestingdemo.exceptions.BankAPIException;
import org.example.javatestingdemo.services.interfaces.BankApiClient;
import org.springframework.stereotype.Service;

@Service
public class BankApiClientImpl implements BankApiClient {
    @Override
    public BankResponse charge(PaymentRequest request) throws BankAPIException {
        throw new UnsupportedOperationException("This should never be called in a unit test.");
    }
}
