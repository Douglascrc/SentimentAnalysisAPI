package com.hackathon.sentiment.api.dto;

import java.util.List;

public record ErrorResponse(
        String comentario,
        Double probabilidade,
        List<String> palavrasChave,
        String mensagem
) {}
