package com.fitcrew.trainerservice.service.handler;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.jwt.model.AuthenticationResponse;
import com.fitcrew.trainerservice.AbstractIntegrationTest;
import com.fitcrew.trainerservice.service.cache.AuthenticationRequestCache;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AuthenticationHandlerTest extends AbstractIntegrationTest {

    private static final TrainerDto trainerDto = TrainerUtil.getTrainerDto();

    @MockBean
    private AuthenticationRequestCache authenticationRequestCache;

    @Test
    void shouldLogin() {
        when(authenticationRequestCache.get(anyString()))
                .thenReturn(Mono.empty());
        doNothing()
                .when(authenticationRequestCache).put(anyString(), any(), anyLong());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/login")
                        .build())
                .body(Mono.just(getAuthenticationRequest()), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthenticationResponse.class);
    }

    @Test
    void shouldSignUpTrainer() {
        trainerDto.setEmail(TRAINER_EMAIL);
        when(authenticationRequestCache.get(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/sign-up")
                        .build())
                .body(Mono.just(trainerDto), TrainerDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainerModel.class);
    }
}