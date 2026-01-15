package com.hackathon.sentiment.api.domain.model;

public class CommentItem {
    
    private String text;
    private String sentiment;
    private Double score;
    private String language;

    public CommentItem(String text, String sentiment, Double score, String language) {
        this.text = text;
        this.sentiment = sentiment;
        this.score = score;
        this.language = language;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
