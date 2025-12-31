package com.hackathon.sentiment.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "request_log") //
@Data
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String endpoint; // Qual URL foi chamada

    private String method; // GET, POST, etc

    @Column(name = "status_code")
    private Integer statusCode; // Ex: 200, 404

    @Column(name = "request_time_ms")
    private Integer requestTimeMs; // Tempo de resposta

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}