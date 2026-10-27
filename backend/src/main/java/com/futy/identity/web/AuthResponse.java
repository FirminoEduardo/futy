package com.futy.identity.web;

public record AuthResponse(
        String token,
        String tipo,
        String email,
        String nome
) {
    public static AuthResponse from(String token, String email, String nome) {
        return new AuthResponse(token, "Bearer", email, nome);
    }
}

