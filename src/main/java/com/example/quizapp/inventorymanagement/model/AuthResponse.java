package com.example.quizapp.inventorymanagement.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AuthResponse {

    private int statusCode;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Map<String,String> errors;
    private LocalDateTime timeStamp;

    public AuthResponse(int statusCode, String message, String accessToken, String refreshToken, Map<String, String> errors, LocalDateTime timeStamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.errors = errors;
        this.timeStamp = timeStamp;
    }

}
