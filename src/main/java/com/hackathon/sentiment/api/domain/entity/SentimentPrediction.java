package com.hackathon.sentiment.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sentiment_prediction") //
@Data
public class SentimentPrediction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String sentiment;

    private Double score;

    @Column(name = "model_version")
    private String modelVersion;

    private String language;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}