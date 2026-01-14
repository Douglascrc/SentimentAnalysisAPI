package com.hackathon.sentiment.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SentimentRequest(
        @NotBlank(message = "O campo 'text' é obrigatório")
        @Size(min = 10, message = "O texto deve ter no mínimo 10 caracteres")
        @JsonProperty("text")
        String text
) {}
