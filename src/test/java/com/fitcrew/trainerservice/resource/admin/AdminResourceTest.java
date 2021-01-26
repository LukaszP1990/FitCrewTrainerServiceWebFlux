package com.fitcrew.trainerservice.resource.admin;

import com.fitcrew.FitCrewAppConstant.message.type.RoleType;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.AbstractIntegrationTest;
import com.fitcrew.trainerservice.service.cache.TrainerModelCache;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AdminResourceTest extends AbstractIntegrationTest {

    @MockBean
    private TrainerModelCache trainerModelCache;

    private static final String ADMIN_EMAIL = "admin@mail.com";

    @Test
    void shouldGetTrainers() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/admin/trainers")
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_ADMIN, ADMIN_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TrainerModel.class)
                .hasSize(3)
                .consumeWith(response ->
                        response.getResponseBody()
                                .forEach(trainerModel ->
                                        assertEquals(TRAINER_FIRST_NAME, trainerModel.getFirstName()))
                );
    }

    @Test
    void shouldDeleteTrainer() {
        doNothing()
                .when(trainerModelCache).delete(anyString());

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/admin")
                        .queryParam("trainer-email", String.valueOf(1).concat(TRAINER_EMAIL))
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_ADMIN, ADMIN_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainerModel.class)
                .value(trainerModel -> checkTrainerModelAssertions(trainerModel, 1));
    }

    @Test
    void shouldUpdateTrainer() {
        var trainerDto = TrainerUtil.getTrainerDto();
        trainerDto.setTrainerId(String.valueOf(100));
        doNothing()
                .when(trainerModelCache).put(anyString(), any(), anyLong());

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/admin")
                        .queryParam("trainer-email", String.valueOf(1).concat(TRAINER_EMAIL))
                        .build())
                .body(Mono.just(trainerDto), TrainerDto.class)
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_ADMIN, ADMIN_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainerModel.class)
                .value(trainerModel -> checkTrainerModelAssertions(trainerModel, 100));
    }

    @Test
    void shouldGetTrainer() {
        when(trainerModelCache.get(anyString()))
                .thenReturn(Mono.empty());
        doNothing()
                .when(trainerModelCache).put(anyString(), any(), anyLong());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/admin")
                        .queryParam("trainer-email", String.valueOf(1).concat(TRAINER_EMAIL))
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_ADMIN, ADMIN_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainerModel.class)
                .value(trainerModel -> checkTrainerModelAssertions(trainerModel, 1));
    }
}