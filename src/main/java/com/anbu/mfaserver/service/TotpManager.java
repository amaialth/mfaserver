package com.anbu.mfaserver.service;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface TotpManager {
    String generateSecretKey();
    String getQRCode(final String secret) throws QrGenerationException;
    boolean verifyTotp(final String code, final String secret);
}
