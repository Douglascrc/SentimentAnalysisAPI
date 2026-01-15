package com.hackathon.sentiment.api.dto;

import com.hackathon.sentiment.api.domain.model.CommentItem;

import java.util.List;

public record CommentHistoryResponse(
        List<CommentItemResponse> comments
) {
}

