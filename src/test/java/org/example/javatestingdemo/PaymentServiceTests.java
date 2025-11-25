package org.example.javatestingdemo;

import org.example.javatestingdemo.dtos.BankResponse;
import org.example.javatestingdemo.dtos.PaymentRequest;
import org.example.javatestingdemo.dtos.PaymentResult;
import org.example.javatestingdemo.dtos.Transaction;
import org.example.javatestingdemo.exceptions.BankAPIException;
import org.example.javatestingdemo.exceptions.InvalidPaymentException;
import org.example.javatestingdemo.exceptions.PaymentException;
import org.example.javatestingdemo.repositories.TransactionRepository;
import org.example.javatestingdemo.services.PaymentServiceImpl;
import org.example.javatestingdemo.services.interfaces.BankApiClient;
import org.example.javatestingdemo.services.interfaces.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {
    @Mock
    private BankApiClient bankClient;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Khi thanh toán thành công, nên trả về transaction ID")
    void givenValidPayment_whenProcess_thenReturnTransactionId() {
        // ===== GIVEN =====
        PaymentRequest request = new PaymentRequest(1000, "VND", "user@email.com");

        // Mock bank API response
        when(bankClient.charge(any(PaymentRequest.class)))
                .thenReturn(new BankResponse(true, "TXN123"));

        // Mock repository save
        when(repository.save(any(Transaction.class)))
                .thenReturn(new Transaction("TXN123"));

        // ===== WHEN =====
        PaymentResult result = paymentService.processPayment(request);

        // ===== THEN =====
        assertNotNull(result);
        assertEquals("TXN123", result.getTransactionId());
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("Khi API ngân hàng lỗi, nên throw PaymentException")
    void givenBankAPIDown_whenProcess_thenThrowException() {
        // ===== GIVEN =====
        PaymentRequest request = new PaymentRequest(1000, "VND", "user@email.com");

        // Mock bank API throw exception
        when(bankClient.charge(any()))
                .thenThrow(new BankAPIException("Service unavailable"));

        // ===== WHEN & THEN =====
        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> paymentService.processPayment(request)
        );

        // Verify exception message
        assertTrue(exception.getMessage().contains("Bank API"));
    }

    @Test
    @DisplayName("Khi thanh toán thành công, nên gửi email thông báo")
    void givenSuccessfulPayment_whenProcess_thenSendNotification() {
        // ===== GIVEN =====
        PaymentRequest request = new PaymentRequest(1000, "VND", "customer@email.com");
        when(bankClient.charge(any()))
                .thenReturn(new BankResponse(true, "TXN123"));

        // ===== WHEN =====
        paymentService.processPayment(request);

        // ===== THEN =====
        // Verify email was sent với đúng parameters
        verify(notificationService).sendEmail(
                eq("customer@email.com"),
                contains("TXN123")
        );

        // Verify email sent exactly once
        verify(notificationService, times(1))
                .sendEmail(any(), any());
    }

    @Test
    @DisplayName("Nên lưu transaction với đúng thông tin")
    void shouldSaveTransactionWithCorrectAmount() {
        // ===== GIVEN =====
        PaymentRequest request = new PaymentRequest(1000, "VND", "user@email.com");
        when(bankClient.charge(any()))
                .thenReturn(new BankResponse(true, "TXN123"));

        // Create captor
        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        // ===== WHEN =====
        paymentService.processPayment(request);

        // ===== THEN =====
        // Capture the argument
        verify(repository).save(captor.capture());

        // Get captured value and verify properties
        Transaction savedTransaction = captor.getValue();
        assertEquals(1000, savedTransaction.getAmount());
        assertEquals("VND", savedTransaction.getCurrency());
        assertEquals("TXN123", savedTransaction.getTransactionId());
        assertNotNull(savedTransaction.getCreatedAt());
    }

    @ParameterizedTest
    @DisplayName("Nên xử lý đúng với nhiều mức giá khác nhau")
    @CsvSource({
            "100,    VND, true,  'Valid small amount'",
            "1000000, VND, true,  'Valid large amount'",
            "0,      VND, false, 'Zero amount should fail'",
            "-100,   VND, false, 'Negative amount should fail'"
    })
    void testMultipleAmounts(
            int amount,
            String currency,
            boolean expectedValid,
            String description
    ) {
        // ===== GIVEN =====
        PaymentRequest request = new PaymentRequest(amount, currency, "user@email.com");

        if (expectedValid) {
            when(bankClient.charge(any()))
                    .thenReturn(new BankResponse(true, "TXN"));
        }

        // ===== WHEN & THEN =====
        if (expectedValid) {
            PaymentResult result = paymentService.processPayment(request);
            assertTrue(result.isSuccess(), description);
        } else {
            assertThrows(
                    InvalidPaymentException.class,
                    () -> paymentService.processPayment(request),
                    description
            );
        }
    }
}
