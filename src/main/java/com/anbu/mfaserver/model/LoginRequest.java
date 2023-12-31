package com.anbu.mfaserver.model;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
}
