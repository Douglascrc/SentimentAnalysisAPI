package com.hackathon.sentiment.api.domain.service;

import com.hackathon.sentiment.api.dto.SentimentRequest;
import com.hackathon.sentiment.api.dto.SentimentResponse;
import com.hackathon.sentiment.api.domain.entity.Comment;
import com.hackathon.sentiment.api.domain.entity.SentimentPrediction;
import com.hackathon.sentiment.api.domain.repository.CommentRepository;
import com.hackathon.sentiment.api.domain.repository.SentimentPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SentimentService {

    @Value("${python.service.url}")
    private String pythonServiceUrl;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SentimentPredictionRepository predictionRepository;

    public SentimentResponse analyze(String text, String language) {
        TextValidationService validationService = new TextValidationService();
        validationService.validate(text);

        Comment newComment = new Comment();
        newComment.setText(text);
        Comment savedComment = commentRepository.save(newComment);

        try {

            RestTemplate restTemplate = new RestTemplate();
            SentimentRequest request = new SentimentRequest(text, language);
            SentimentResponse response = restTemplate.postForObject(pythonServiceUrl, request, SentimentResponse.class);

            if (response != null) {
                SentimentPrediction prediction = new SentimentPrediction();
                prediction.setComment(savedComment);
                prediction.setSentiment(response.previsao());
                prediction.setScore(response.probabilidade());
                prediction.setLanguage(response.idioma());
                prediction.setModelVersion("1.1");

                predictionRepository.save(prediction);

                return response;
            } else {
                throw new RuntimeException("Resposta invalida do serviço Python");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao chamar o serviço Python: " + ex.getMessage());
        }
    }

}