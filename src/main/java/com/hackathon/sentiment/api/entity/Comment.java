package com.hackathon.sentiment.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity // Diz ao Hibernate que isto é uma tabela do banco
@Table(name = "comment") // Define o nome da tabela
@Data // Gera automaticamente Getters e Setters (Lombok)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; // Chave primária UUID

    @Column(columnDefinition = "TEXT")
    private String text; // O feedback do usuário

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // Data de criação
}