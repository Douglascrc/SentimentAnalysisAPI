package com.hackathon.sentiment.api.exception;

import com.hackathon.sentiment.api.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarBadRequestQuandoIllegalArgumentException() {
        IllegalArgumentException exception =
                new IllegalArgumentException("Texto inválido");

        ResponseEntity<ErrorResponse> response =
                handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro", body.comentario());
        assertEquals("Texto inválido", body.mensagem());
    }

    @Test
    void deveRetornarServiceUnavailableQuandoRestClientException() {
        RestClientException exception =
                new RestClientException("Falha externa");

        ResponseEntity<ErrorResponse> response =
                handler.handleRestClientException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(
                "Serviço de análise temporariamente indisponível. Tente novamente em instantes.",
                body.mensagem()
        );
    }

    @Test
    void deveRetornarInternalServerErrorQuandoExceptionGenerica() {
        Exception exception =
                new RuntimeException("Erro inesperado");

        ResponseEntity<ErrorResponse> response =
                handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(
                "Não foi possível realizar a classificação no momento. Tente novamente mais tarde.",
                body.mensagem()
        );
    }
}
