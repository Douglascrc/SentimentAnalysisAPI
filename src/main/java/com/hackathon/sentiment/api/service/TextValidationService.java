package com.hackathon.sentiment.api.service;

public class TextValidationService {

    public void validate(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("O campo text não pode ser nulo ou vazio");
        }

        if (text.length() < 10) {
            throw new IllegalArgumentException("O texto deve ter no mínimo 10 caracteres");
        }

        if (text.matches("\\d+")) {
            throw new IllegalArgumentException("O texto não pode conter apenas números");
        }
    }
}