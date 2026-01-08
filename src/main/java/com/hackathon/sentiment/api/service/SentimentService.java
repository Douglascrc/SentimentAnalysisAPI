package com.hackathon.sentiment.api.service;

import com.hackathon.sentiment.api.dto.SentimentRequest;
import com.hackathon.sentiment.api.dto.SentimentResponse;
import com.hackathon.sentiment.api.entity.Comment;
import com.hackathon.sentiment.api.entity.SentimentPrediction;
import com.hackathon.sentiment.api.repository.CommentRepository;
import com.hackathon.sentiment.api.repository.SentimentPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SentimentService {

    @Value("${env.PYTHON_SERVICE_URL}")
    private String pythonServiceUrl;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SentimentPredictionRepository predictionRepository;

    public SentimentResponse analyze(String text) {
        TextValidationService validationService = new TextValidationService();
        validationService.validate(text);

        Comment newComment = new Comment();
        newComment.setText(text);
        Comment savedComment = commentRepository.save(newComment);

        RestTemplate restTemplate = new RestTemplate();
        SentimentRequest request = new SentimentRequest(text);

        SentimentResponse response = restTemplate.postForObject(pythonServiceUrl, request, SentimentResponse.class);

        if (response != null) {
            SentimentPrediction prediction = new SentimentPrediction();
            prediction.setComment(savedComment);
            prediction.setSentiment(response.previsao());
            prediction.setScore(response.probabilidade());
            prediction.setModelVersion("1.0");

            predictionRepository.save(prediction);

            return response;
        }

        throw new RuntimeException("Erro no serviço de previsão de sentimento");
    }

}