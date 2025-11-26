package org.example.javatestingdemo.services;

import org.example.javatestingdemo.dtos.BankResponse;
import org.example.javatestingdemo.dtos.PaymentRequest;
import org.example.javatestingdemo.dtos.PaymentResult;
import org.example.javatestingdemo.dtos.Transaction;
import org.example.javatestingdemo.exceptions.BankAPIException;
import org.example.javatestingdemo.exceptions.InvalidPaymentException;
import org.example.javatestingdemo.exceptions.PaymentException;
import org.example.javatestingdemo.repositories.TransactionRepository;
import org.example.javatestingdemo.services.interfaces.BankApiClient;
import org.example.javatestingdemo.services.interfaces.NotificationService;
import org.example.javatestingdemo.services.interfaces.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final BankApiClient bankClient;
    private final NotificationService notificationService;
    private final TransactionRepository repository;

    public PaymentServiceImpl(BankApiClient bankClient,
                          NotificationService notificationService,
                          TransactionRepository repository) {
        this.bankClient = bankClient;
        this.notificationService = notificationService;
        this.repository = repository;
    }

    public PaymentResult processPayment(PaymentRequest request) throws PaymentException {
        if (true) {
            throw new RuntimeException("Not implemented yet");
        }
        // 1. Validate request
        if (request.getAmount() <= 0) {
            throw new InvalidPaymentException("Amount must be positive");
        }

        // 2. Call bank API
        BankResponse response;
        try {
            response = bankClient.charge(request);
        } catch (BankAPIException e) {
            // Wrap external exception into domain exception
            throw new PaymentException("Failed to communicate with Bank API: " + e.getMessage(), e);
        }

        // 3. Save transaction
        var transaction = new Transaction(
                response.getTransactionId(),
                request.getAmount(),
                request.getCurrency()
        );
        repository.save(transaction);

        // 4. Send notification
        notificationService.sendEmail(
                request.getCustomerEmail(),
                "Payment successful: " + response.getTransactionId()
        );

        // 5. Return result
        return new PaymentResult(
                response.getTransactionId(),
                true
        );
    }
}
