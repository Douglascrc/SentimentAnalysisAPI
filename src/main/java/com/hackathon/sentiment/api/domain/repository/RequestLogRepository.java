package com.hackathon.sentiment.api.domain.repository;

import com.hackathon.sentiment.api.domain.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {

    // Esta query busca a média da coluna request_time_ms que consta no PDF que enviei.
    // O Spring Data JPA mapeia automaticamente o nome do campo CamelCase (requestTimeMs)
    // para o padrão do banco (request_time_ms).
    @Query("SELECT AVG(r.requestTimeMs) FROM RequestLog r")
    Double getAverageResponseTime();
}