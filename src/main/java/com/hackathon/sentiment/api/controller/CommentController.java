package com.hackathon.sentiment.api.controller;

import com.hackathon.sentiment.api.domain.service.CommentService;
import com.hackathon.sentiment.api.dto.CommentHistoryResponse;

import com.hackathon.sentiment.api.dto.CommentItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<CommentHistoryResponse> getHistory() {
        var domainHistory = commentService.findAllCommentsWithPredictions();

        List<CommentItemResponse> responseList = domainHistory.stream()
                .map(item -> new CommentItemResponse(
                        item.getText(),
                        item.getSentiment(),
                        item.getScore(),
                        item.getLanguage()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CommentHistoryResponse(responseList));
    }
}
