package com.anbu.mfaserver.service;

import com.anbu.mfaserver.exception.InvalidTokenException;
import com.anbu.mfaserver.exception.UserAlreadyExistException;
import com.anbu.mfaserver.model.MfaTokenData;
import com.anbu.mfaserver.model.User;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.mail.MessagingException;

public interface UserService {
    MfaTokenData registerUser(User user) throws UserAlreadyExistException, QrGenerationException;
    //MfaTokenData mfaSetup(String email) throws UnkownIdentifierException, QrGenerationException;
    boolean verifyTotp(final String code,String username);

    void sendRegistrationConfirmationEmail(final User user) throws MessagingException;
    boolean verifyUser(final String token) throws InvalidTokenException;
}
