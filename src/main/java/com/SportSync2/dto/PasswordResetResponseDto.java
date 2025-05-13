package com.SportSync2.dto;

public class PasswordResetResponseDto {
    private String message;
    private String token;

    public PasswordResetResponseDto(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
