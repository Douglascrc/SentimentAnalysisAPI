package com.hackathon.sentiment.api.exception;

import com.hackathon.sentiment.api.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void deveRetornarErroPadronizadoQuandoOcorrerExcecaoGenerica() {
        // arrange (preparação)
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception exception = new RuntimeException("Erro inesperado");

        // act (ação)
        ResponseEntity<ErrorResponse> response =
                handler.handleGenericException(exception);

        // assert (validações)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);

        assertEquals("Erro", body.comentario());
        assertEquals(0.0, body.probabilidade());
        assertTrue(body.palavrasChave().isEmpty());
        assertEquals(
                "Não foi possível realizar a classificação no momento. Tente novamente mais tarde.",
                body.mensagem()
        );
    }
}