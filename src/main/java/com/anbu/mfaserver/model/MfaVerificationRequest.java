package com.anbu.mfaserver.model;

import lombok.Data;

@Data
public class MfaVerificationRequest {
    private String username;
    private String totp;
}
