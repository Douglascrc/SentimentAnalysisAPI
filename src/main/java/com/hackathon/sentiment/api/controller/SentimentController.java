package com.hackathon.sentiment.api.controller;

import com.hackathon.sentiment.api.dto.SentimentRequest;
import com.hackathon.sentiment.api.dto.SentimentResponse;
import com.hackathon.sentiment.api.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável por receber as requisições de análise de sentimento.
 * A anotação @CrossOrigin com "*" permite que o Frontend em qualquer porta (5173 ou 5174)
 * consiga ler a resposta da IA.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sentiment")
public class SentimentController {

    @Autowired
    private SentimentService sentimentService;

    /**
     * Endpoint POST que recebe o texto e devolve a predição.
     * Alterado para retornar ResponseEntity.ok para maior compatibilidade com o Dashboard.
     */
    @PostMapping
    public ResponseEntity<SentimentResponse> analyzeSentiment(@RequestBody SentimentRequest request) {
        // Extrai o texto do DTO recebido
        var textValue = request.text();

        // Chama o serviço que integra com o modelo de IA e salva no banco Postgres
        SentimentResponse result = sentimentService.analyze(textValue);

        // Retorna o resultado com Status 200 (OK) para o React
        return ResponseEntity.ok(result);
    }
}