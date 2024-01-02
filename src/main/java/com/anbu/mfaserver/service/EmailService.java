package com.anbu.mfaserver.service;

import com.anbu.mfaserver.model.EmailConfirmationToken;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendConfirmationEmail(EmailConfirmationToken emailConfirmationToken) throws MessagingException;
}
