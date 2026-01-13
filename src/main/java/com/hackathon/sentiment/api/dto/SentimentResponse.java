package com.hackathon.sentiment.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Record ajustado para os nomes que o Dashboard React espera.
 * Isso resolve o erro de "NaN%" e "0.00%".
 */
public record SentimentResponse (
        String text,           // O texto original
        String sentiment,      // "POSITIVO", "NEGATIVO" ou "NEUTRO"
        Double score,          // A probabilidade (0.0 a 1.0)
        List<String> keywords  // Palavras-chave encontradas
){}
