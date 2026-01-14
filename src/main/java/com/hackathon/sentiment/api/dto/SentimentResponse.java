package com.hackathon.sentiment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SentimentResponse (
        String previsao,
        Double probabilidade,
        @JsonProperty("palavras_chave")
        List<String> keywords
){}
