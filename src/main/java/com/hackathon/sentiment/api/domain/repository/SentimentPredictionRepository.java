package com.hackathon.sentiment.api.domain.repository;

import com.hackathon.sentiment.api.domain.entity.SentimentPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SentimentPredictionRepository extends JpaRepository<SentimentPrediction, UUID> {


    long countBySentiment(String sentiment);

    @Query("SELECT p FROM SentimentPrediction p JOIN FETCH p.comment")
    List<SentimentPrediction> findAllWithComments();
}