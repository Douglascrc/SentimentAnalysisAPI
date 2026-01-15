package com.hackathon.sentiment.api.dto;

public record CommentItemResponse(
        String text,
        String sentiment,
        Double score,
        String language
) {
}
