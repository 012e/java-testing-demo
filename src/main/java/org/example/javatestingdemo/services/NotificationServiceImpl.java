package org.example.javatestingdemo.services;

import org.example.javatestingdemo.services.interfaces.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendEmail(String recipient, String message) {
        // Simulates sending a real email
        throw new UnsupportedOperationException("This should never be called in a unit test.");
    }
}
