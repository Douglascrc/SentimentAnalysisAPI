package com.hackathon.sentiment.api.controller;

import com.hackathon.sentiment.api.domain.service.StatsService;
import com.hackathon.sentiment.api.dto.StatsResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.bind.annotation.CrossOrigin; // Importado para o Dashboard
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173") // Libera o acesso para o React
@RestController
@RequestMapping("/stats") // Ajustado para bater com o relatório técnico
public class StatsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private HealthEndpoint healthEndpoint;

    @GetMapping
    public StatsResponseDTO getStats() {

        StatsResponseDTO stats = statsService.obterEstatisticas();

        Status healthStatus = healthEndpoint.health().getStatus();
        stats.setStatusApi(healthStatus.getCode());

        return stats;
    }
}