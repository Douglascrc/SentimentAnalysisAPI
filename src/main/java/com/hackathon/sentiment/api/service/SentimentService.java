package com.hackathon.sentiment.api.service;

import com.hackathon.sentiment.api.dto.SentimentResponse;

import java.util.List;

public class SentimentService {
    
    private final static List<String> positiveWords = List.of("bom", "ótimo", "excelente", "maravilhoso", "fantástico", "positivo", "satisfatório", "agradável");
    private final static List<String> negativeWords = List.of("ruim", "péssimo", "horrível", "terrível", "lamentável", "negativo", "insatisfatório", "desagradável");

    public static SentimentResponse analyze(String text) {
        if(positiveWords.stream().anyMatch(text::contains)) {
            return new SentimentResponse(
                text,
                0.9,
                    List.of(positiveWords.stream().filter(text::contains).findFirst().get())
            );
        } else if(negativeWords.stream().anyMatch(text::contains)) {
            return new SentimentResponse(
                text,
                    0.9,
                List.of(negativeWords.stream().filter(text::contains).findFirst().get())
            );
        }
        return new SentimentResponse(
            text,
                0.5,
            List.of()
        );
    }
}
