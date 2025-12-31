package com.hackathon.sentiment.api.repository;

import com.hackathon.sentiment.api.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
}