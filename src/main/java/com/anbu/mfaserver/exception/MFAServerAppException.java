package com.anbu.mfaserver.exception;

public class MFAServerAppException extends RuntimeException{
    public MFAServerAppException(String message) {
        super(message);
    }

    public MFAServerAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
