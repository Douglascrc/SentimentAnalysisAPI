package com.hackathon.sentiment.api.dto;

import java.util.List;

public record SentimentResponse (
  String comentario,
  Double probabilidade,
  List<String> palavrasChave
){}
