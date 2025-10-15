package com.example.demo.shared.exception;

public class MesaOcupadaException extends RuntimeException {

    public MesaOcupadaException(String message) {
        super(message);
    }

    public MesaOcupadaException(String message, Throwable cause) {
        super(message, cause);
    }
}