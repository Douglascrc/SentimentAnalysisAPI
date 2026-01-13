package com.hackathon.sentiment.api.service;

import com.hackathon.sentiment.api.dto.SentimentResponse;
import com.hackathon.sentiment.api.entity.Comment;
import com.hackathon.sentiment.api.entity.SentimentPrediction;
import com.hackathon.sentiment.api.repository.CommentRepository;
import com.hackathon.sentiment.api.repository.SentimentPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class SentimentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SentimentPredictionRepository predictionRepository;

    // Ajustado para radicais para aumentar a abrangência (Ex: satisf pega satisfeito e satisfação)
    private final static List<String> positiveWords = List.of(
            "bom", "ótim", "excelent", "maravilho", "fantástic",
            "positiv", "satisf", "agradá", "entregue", "prazo", "chegou"
    );

    private final static List<String> negativeWords = List.of(
            "ruim", "péssim", "horrív", "terrív", "lamentá",
            "negativ", "insatisf", "desagradá", "atraso", "atrasad",
            "quebrad", "faltand", "defeit", "abert", "estragad"
    );

    public SentimentResponse analyze(String text) {
        Comment comment = new Comment();
        comment.setText(text);
        comment = commentRepository.save(comment);

        String lowerText = text.toLowerCase();
        String sentimentLabel = "NEUTRO";
        double score = 0.5;
        String modelName = "Olist";
        List<String> finalKeywords = new ArrayList<>();

        // 1. Identificando os radicais presentes na frase
        List<String> foundPositives = positiveWords.stream()
                .filter(lowerText::contains)
                .collect(Collectors.toList());

        List<String> foundNegatives = negativeWords.stream()
                .filter(lowerText::contains)
                .collect(Collectors.toList());

        // 2. Lógica de Prioridade: O negativo "insatisf" ganha do positivo "satisf"
        if (!foundNegatives.isEmpty()) {
            sentimentLabel = "NEGATIVO";
            score = 0.05;
            modelName = "B2W";
            finalKeywords = foundNegatives;
        }
        // 3. Se não houver negativos, o radical positivo é validado
        else if (!foundPositives.isEmpty()) {
            sentimentLabel = "POSITIVO";
            score = 0.95;
            modelName = "B2W";
            finalKeywords = foundPositives;
        }

        SentimentPrediction prediction = new SentimentPrediction();
        prediction.setComment(comment);
        prediction.setSentiment(sentimentLabel);
        prediction.setScore(score);
        prediction.setModelVersion(modelName);
        predictionRepository.save(prediction);

        return new SentimentResponse(text, sentimentLabel, score, finalKeywords);
    }
}