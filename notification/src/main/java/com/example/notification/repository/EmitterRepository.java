package com.example.notification.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class EmitterRepository {

    private Map<String, SseEmitter> emitterMap = new HashMap<>();

    public SseEmitter save(Integer userId, SseEmitter sseEmitter) {
        final String key = getKey(userId);
        emitterMap.put(key, sseEmitter);
        log.info("Set SSE Emitter {}", userId);
        return sseEmitter;
    }

    public Optional<SseEmitter> get(Integer userId) {
        final String key = getKey(userId);
        log.info("Get SSE Emitter {}", userId);
        return Optional.ofNullable(emitterMap.get(key));
    }

    public void delete(Integer userId) {
        emitterMap.remove(getKey(userId));
    }

    private String getKey(Integer userId) {
        return "Emitter:UID:" + userId;
    }
}
