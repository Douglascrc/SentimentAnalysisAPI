package com.hackathon.sentiment.api.controller;

import com.hackathon.sentiment.api.dto.SentimentRequest;
import com.hackathon.sentiment.api.dto.SentimentResponse;
import com.hackathon.sentiment.api.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/sentiment")
public class SentimentController {

    @Autowired
    private SentimentService sentimentService; // Injetei o serviço para poder salvar no banco

    @PostMapping
    public ResponseEntity<SentimentResponse> analyzeSentiment(@RequestBody SentimentRequest request, UriComponentsBuilder uriBuilder) {
        var textValue = request.text();

        // Agora chamei o método da instância (sem o 'static')
        SentimentResponse result = sentimentService.analyze(textValue);

        // Criei a URI de retorno. Como o ID é UUID automático, simplificamos o caminho
        var uri = uriBuilder.path("/sentiment").build().toUri();

        return ResponseEntity.created(uri).body(result);
    }
}