package com.hackathon.sentiment.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sentiment_prediction") //
@Data
public class SentimentPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Aqui está a ligação (Foreign Key) do diagrama
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String sentiment; // Ex: "Positivo"

    private Double score; // O valor numérico da predição

    @Column(name = "model_version")
    private String modelVersion; // Versão do modelo usado no Colab

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}