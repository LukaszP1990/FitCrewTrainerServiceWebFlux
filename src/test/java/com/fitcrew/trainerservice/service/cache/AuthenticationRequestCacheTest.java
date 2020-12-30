package com.fitcrew.trainerservice.service.cache;

import com.fitcrew.FitCrewAppConstant.message.type.RoleType;
import com.fitcrew.jwt.model.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Objects;

import static com.fitcrew.trainerservice.util.TrainerUtil.PASSWORD;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationRequestCacheTest {

    @Mock
    private ReactiveRedisConnectionFactory factory;
    @Mock
    private ReactiveRedisOperations<String, AuthenticationRequest> authenticationOps;
    @Mock
    private ReactiveValueOperations<String, AuthenticationRequest> reactiveValueOperations;

    @InjectMocks
    private AuthenticationRequestCache authenticationRequestCache;

    @Test
    void put() {
        ReflectionTestUtils.setField(authenticationRequestCache, "factory", factory);
        ReflectionTestUtils.setField(authenticationRequestCache, "authenticationOps", authenticationOps);
        when(authenticationOps.opsForValue())
                .thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.set(anyString(), any(), any()))
                .thenReturn(Mono.just(true));

        authenticationRequestCache.put(TRAINER_EMAIL, getAuthenticationRequest(), 1);

        verify(reactiveValueOperations, times(1)).set(TRAINER_EMAIL, getAuthenticationRequest(), Duration.ofHours(1));
    }

    @Test
    void get() {
        ReflectionTestUtils.setField(authenticationRequestCache, "factory", factory);
        ReflectionTestUtils.setField(authenticationRequestCache, "authenticationOps", authenticationOps);
        when(authenticationOps.opsForValue())
                .thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.get(anyString()))
                .thenReturn(Mono.just(getAuthenticationRequest()));

        StepVerifier.create(authenticationRequestCache.get(TRAINER_EMAIL))
                .expectSubscription()
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

    public AuthenticationRequest getAuthenticationRequest() {
        return new AuthenticationRequest(String.valueOf(1).concat(TRAINER_EMAIL), PASSWORD, RoleType.ROLE_ADMIN);
    }
}