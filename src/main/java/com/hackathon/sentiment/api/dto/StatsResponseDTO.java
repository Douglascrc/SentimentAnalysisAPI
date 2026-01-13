package com.hackathon.sentiment.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponseDTO {

    // Saúde da aplicação (Virá do Actuator)
    // Inicializado como "DOWN" por segurança até que a Service confirme o "UP"
    private String statusApi = "DOWN";

    // Estatísticas de Negócio (Virá das tabelas de Sentiment)
    // Usei 0L para Long e 0.0 para Double para evitar o "null" no JSON
    private Long totalAnalises = 0L;
    private Double percentualPositivo = 0.0;
    private Double percentualNegativo = 0.0;

    // Performance (Virá da tabela de logs)
    // Agora inicializado com 0.0 em vez de null
    private Double tempoMedioRespostaMs = 0.0;
}