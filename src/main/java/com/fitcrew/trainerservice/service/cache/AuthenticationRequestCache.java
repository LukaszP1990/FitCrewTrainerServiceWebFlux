package com.fitcrew.trainerservice.service.cache;

import com.fitcrew.jwt.model.AuthenticationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class AuthenticationRequestCache {

    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, AuthenticationRequest> authenticationOps;

    public AuthenticationRequestCache(ReactiveRedisConnectionFactory factory,
                                      ReactiveRedisOperations<String, AuthenticationRequest> authenticationOps) {
        this.factory = factory;
        this.authenticationOps = authenticationOps;
    }

    public void put(String key,
                    AuthenticationRequest authenticationRequest,
                    long hour) {
        authenticationOps.opsForValue().set(key, authenticationRequest, Duration.ofHours(hour))
                .subscribe(result -> log.info("Add authenticationRequest: {} to cache", authenticationRequest));
    }

    public Mono<AuthenticationRequest> get(String key) {
        log.info("Get authenticationRequest by key: {} from cache", key);
        return authenticationOps.opsForValue().get(key);
    }
}
