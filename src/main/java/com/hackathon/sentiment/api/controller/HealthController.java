package com.hackathon.sentiment.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Value("${python.service.url}")
    private String pythonServiceUrl;

    @GetMapping("/warmup")
    public ResponseEntity<Map<String, String>> warmup() {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Chama o Python ML para acordá-lo (sem salvar no banco)
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> request = new HashMap<>();
            request.put("text", "warmup");
            request.put("language", "pt");
            
            restTemplate.postForObject(pythonServiceUrl, request, Object.class);
            
            response.put("status", "Backend e ML aquecidos");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Backend OK, ML dormindo");
            return ResponseEntity.ok(response);
        }
    }
}
