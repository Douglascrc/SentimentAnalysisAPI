package com.hackathon.sentiment.api.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextValidationServiceTest {

    private final TextValidationService service = new TextValidationService();

    @Test
    void devePassarQuandoTextoForValido() {
        assertDoesNotThrow(() ->
                service.validate("O atendimento foi excelente"));
    }

    @Test
    void deveFalharQuandoTextoForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.validate(null));
    }

    @Test
    void deveFalharQuandoTextoForMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () ->
                service.validate("oi"));
    }

    @Test
    void deveFalharQuandoTextoForSomenteNumeros() {
        assertThrows(IllegalArgumentException.class, () ->
                service.validate("1234567890"));
    }
}