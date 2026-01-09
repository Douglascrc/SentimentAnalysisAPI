package com.hackathon.sentiment.api.service;

import com.hackathon.sentiment.api.dto.StatsResponseDTO;
import com.hackathon.sentiment.api.repository.SentimentPredictionRepository;
import com.hackathon.sentiment.api.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint; // Sensor de saúde
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    @Autowired
    private SentimentPredictionRepository predictionRepository;

    @Autowired
    private RequestLogRepository logRepository;

    @Autowired
    private HealthEndpoint healthEndpoint; // Injeção do Actuator

    public StatsResponseDTO obterEstatisticas() {
        StatsResponseDTO dto = new StatsResponseDTO();

        // 1. Verificação de Saúde Real do Sistema
        // Em vez de "UP" fixo, agora o Spring retorna o status real.
        Status healthStatus = healthEndpoint.health().getStatus();
        dto.setStatusApi(healthStatus.getCode());

        long total = predictionRepository.count();

        // 2. Cálculos de Sentimento com as 2 casas decimais.
        if (total > 0) {
            long positivos = predictionRepository.countBySentiment("Positivo");
            long negativos = predictionRepository.countBySentiment("Negativo");

            dto.setTotalAnalises(total);

            double percPos = (positivos * 100.0) / total;
            double percNeg = (negativos * 100.0) / total;

            dto.setPercentualPositivo(Math.round(percPos * 100.0) / 100.0);
            dto.setPercentualNegativo(Math.round(percNeg * 100.0) / 100.0);
        }

        // 3. Performance baseada na média da coluna request_time_ms.
        Double media = logRepository.getAverageResponseTime();

        if (media != null) {
            // Mantém o arredondamento para 1 casa decimal: ex: 151.0
            dto.setTempoMedioRespostaMs(Math.round(media * 10.0) / 10.0);
        } else {
            dto.setTempoMedioRespostaMs(0.0);
        }

        return dto;
    }
}