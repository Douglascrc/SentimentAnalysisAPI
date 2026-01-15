package com.hackathon.sentiment.api.domain.repository;

import com.hackathon.sentiment.api.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// JpaRepository é o que faz a "mágica" de salvar no H2 ou Postgres sem você escrever SQL
public interface CommentRepository extends JpaRepository<Comment, UUID> {
}