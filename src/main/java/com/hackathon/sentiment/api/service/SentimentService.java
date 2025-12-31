package com.hackathon.sentiment.api.service;

import com.hackathon.sentiment.api.dto.SentimentResponse;
import com.hackathon.sentiment.api.entity.Comment;
import com.hackathon.sentiment.api.entity.SentimentPrediction;
import com.hackathon.sentiment.api.repository.CommentRepository;
import com.hackathon.sentiment.api.repository.SentimentPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indica que esta classe é um serviço gerenciado pelo Spring
public class SentimentService {

    @Autowired
    private CommentRepository commentRepository; // Injeção do repositório de comentários

    @Autowired
    private SentimentPredictionRepository predictionRepository; // Injeção do repositório de previsões

    private final static List<String> positiveWords = List.of("bom", "ótimo", "excelente", "maravilhoso", "fantástico", "positivo", "satisfatório", "agradável");
    private final static List<String> negativeWords = List.of("ruim", "péssimo", "horrível", "terrível", "lamentável", "negativo", "insatisfatório", "desagradável");

    // Removido o 'static' para permitir o uso dos repositórios acima
    public SentimentResponse analyze(String text) {

        // 1. Criar e Persistir o Comentário original
        Comment comment = new Comment();
        comment.setText(text);
        comment = commentRepository.save(comment);

        // 2. Lógica de Análise (Baseada no seu código original)
        String sentimentLabel = "NEUTRAL";
        double score = 0.5;
        List<String> foundKeywords = List.of();

        if (positiveWords.stream().anyMatch(text::contains)) {
            sentimentLabel = "POSITIVE";
            score = 0.9;
            foundKeywords = List.of(positiveWords.stream().filter(text::contains).findFirst().get());
        } else if (negativeWords.stream().anyMatch(text::contains)) {
            sentimentLabel = "NEGATIVE";
            score = 0.1; // Ajustado para 0.1 por ser negativo
            foundKeywords = List.of(negativeWords.stream().filter(text::contains).findFirst().get());
        }

        // 3. PERSISTIR a Previsão no Banco (O objetivo do Card do Trello!)
        SentimentPrediction prediction = new SentimentPrediction();
        prediction.setComment(comment); // Vincula ao comentário salvo através da FK
        prediction.setSentiment(sentimentLabel);
        prediction.setScore(score);
        prediction.setModelVersion("v1-rule-based"); // Identifica qual lógica gerou o dado

        predictionRepository.save(prediction); // Salva de fato no banco de dados

        // 4. Retornar a resposta para o usuário (DTO)
        return new SentimentResponse(text, score, foundKeywords);
    }
}