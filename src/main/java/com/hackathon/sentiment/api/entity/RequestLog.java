package com.hackathon.sentiment.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "request_log") //
@Data
public class RequestLog {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String endpoint; // Qual URL foi chamada

    private String method; // GET, POST, etc

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "request_time_ms")
    private Integer requestTimeMs;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}