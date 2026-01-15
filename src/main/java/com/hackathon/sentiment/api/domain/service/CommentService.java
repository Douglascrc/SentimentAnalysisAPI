package com.hackathon.sentiment.api.domain.service;

import com.hackathon.sentiment.api.domain.model.CommentItem;
import com.hackathon.sentiment.api.domain.repository.SentimentPredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private SentimentPredictionRepository predictionRepository;

    public List<CommentItem> findAllCommentsWithPredictions() {
        return predictionRepository.findAllWithComments().stream()
                .map(prediction -> new CommentItem(
                        prediction.getComment() != null ? prediction.getComment().getText() : null,
                        prediction.getSentiment(),
                        prediction.getScore(),
                        prediction.getLanguage()
                ))
                .collect(Collectors.toList());
    }
}
