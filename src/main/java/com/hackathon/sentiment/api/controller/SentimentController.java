package com.hackathon.sentiment.api.controller;

import com.hackathon.sentiment.api.domain.service.SentimentService;
import com.hackathon.sentiment.api.dto.SentimentRequest;
import com.hackathon.sentiment.api.dto.SentimentResponse;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sentiment")
public class SentimentController {

    @Autowired
    private SentimentService sentimentService;

    @PostMapping
    @Transactional
    public ResponseEntity<SentimentResponse> analyzeSentiment(@Valid @RequestBody SentimentRequest request, UriComponentsBuilder uriBuilder) {
        var textValue = request.text();
        var languageValue = request.language();

        SentimentResponse result = sentimentService.analyze(textValue, languageValue);

        var uri = uriBuilder.path("/sentiment").build().toUri();

        return ResponseEntity.created(uri).body(result);
    }
}