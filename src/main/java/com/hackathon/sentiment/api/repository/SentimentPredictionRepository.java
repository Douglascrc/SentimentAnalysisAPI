package com.hackathon.sentiment.api.repository;

import com.hackathon.sentiment.api.entity.SentimentPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SentimentPredictionRepository extends JpaRepository<SentimentPrediction, UUID> {
}