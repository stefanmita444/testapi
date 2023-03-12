package com.example.testapi.models;

public class EzerException extends RuntimeException {

    private int errorCode;

    public EzerException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
