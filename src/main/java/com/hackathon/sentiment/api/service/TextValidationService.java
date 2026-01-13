package com.hackathon.sentiment.api.service;

public class TextValidationService {

    public void validate(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("O campo text não pode ser nulo ou vazio");
        }

        if (text.length() < 5) {
            throw new IllegalArgumentException("O texto deve ter no mínimo 5 caracteres");
        }

        if (text.matches("\\d+")) {
            throw new IllegalArgumentException("O texto não pode conter apenas números");
     
        }
    }
}