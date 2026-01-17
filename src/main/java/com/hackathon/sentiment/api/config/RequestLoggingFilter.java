package com.hackathon.sentiment.api.config;

import com.hackathon.sentiment.api.domain.entity.RequestLog;
import com.hackathon.sentiment.api.domain.repository.RequestLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestLogRepository requestLogRepository;

    public RequestLoggingFilter(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            RequestLog log = new RequestLog();
            log.setEndpoint(request.getRequestURI());
            log.setMethod(request.getMethod());
            log.setStatusCode(response.getStatus());
            log.setRequestTimeMs((int) duration);
            log.setCreatedAt(LocalDateTime.now());

            requestLogRepository.save(log);
        }
    }
}
