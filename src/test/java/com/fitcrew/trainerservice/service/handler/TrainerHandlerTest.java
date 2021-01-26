package com.fitcrew.trainerservice.service.handler;

import com.fitcrew.FitCrewAppConstant.message.type.RoleType;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainingDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.trainerservice.AbstractIntegrationTest;
import com.fitcrew.trainerservice.feignclient.FeignTrainingService;
import com.fitcrew.trainerservice.service.cache.TrainerModelCache;
import com.fitcrew.trainerservice.util.TrainingUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static com.fitcrew.trainerservice.util.TrainingUtil.TRAINING_NAME;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TrainerHandlerTest extends AbstractIntegrationTest {

    private static final List<String> clientsNames = Arrays.asList("firstName", "secondName");
    private static final List<TrainingModel> trainingModels = TrainingUtil.getTrainingModels();
    private static final TrainingModel trainingModel = TrainingUtil.getTrainingModel();
    private static final TrainingDto trainingDto = TrainingUtil.getTrainingDto();

    @MockBean
    private FeignTrainingService feignTrainingService;

    @MockBean
    private TrainerModelCache trainerModelCache;

    @Test
    void shouldGetClientsWhoGetTrainingFromTrainer() {
        when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
                .thenReturn(Flux.fromIterable(clientsNames));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/trainings/bought")
                        .queryParam("training-name", TRAINING_NAME)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class)
                .hasSize(1);
    }

    @Test
    void shouldGetBasicInformationAboutTrainer() {
        var email = String.valueOf(1).concat(TRAINER_EMAIL);
        when(trainerModelCache.get(anyString()))
                .thenReturn(Mono.empty());
        doNothing()
                .when(trainerModelCache).put(anyString(), any(), anyLong());
        doNothing()
                .when(trainerModelCache).delete(anyString());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/basic/info")
                        .queryParam("trainer-email", email)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainerModel.class)
                .value(trainerModel -> checkTrainerModelAssertions(trainerModel, 1));
    }

    @Test
    void shouldGetTrainerTrainings() {
        var email = String.valueOf(1).concat(TRAINER_EMAIL);
        when(feignTrainingService.getTrainerTrainings(anyString()))
                .thenReturn(Flux.fromIterable(trainingModels));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/trainings")
                        .queryParam("trainer-email", email)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TrainingModel.class)
                .hasSize(3);
    }

    @Test
    void shouldCreateTraining() {
        when(feignTrainingService.createTraining(any()))
                .thenReturn(Mono.just(trainingModel));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/create/training")
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .body(Mono.just(trainingDto), TrainingDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainingModel.class)
                .value(this::checkTrainingModelAssertions);
    }

    @Test
    void shouldDeleteTraining() {
        var email = String.valueOf(1).concat(TRAINER_EMAIL);
        when(feignTrainingService.deleteTraining(anyString(), anyString()))
                .thenReturn(Mono.just(trainingModel));

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/delete/training")
                        .queryParam("trainer-email", email)
                        .queryParam("training-name", TRAINING_NAME)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainingModel.class)
                .value(this::checkTrainingModelAssertions);
    }

    @Test
    void shouldUpdateTraining() {
        when(feignTrainingService.updateTraining(any(), anyString()))
                .thenReturn(Mono.just(trainingModel));
        trainingDto.setTrainerEmail(String.valueOf(100).concat(TRAINER_EMAIL));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/update/training")
                        .queryParam("training-name", TRAINING_NAME)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .body(Mono.just(trainingDto), TrainingDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainingModel.class)
                .value(this::checkTrainingModelAssertions);
    }

    @Test
    void shouldSelectTrainingToSend() {
        var email = String.valueOf(1).concat(TRAINER_EMAIL);
        when(feignTrainingService.selectTraining(anyString(), anyString()))
                .thenReturn(Mono.just(trainingModel));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/select/training")
                        .queryParam("trainer-email", email)
                        .queryParam("training-name", TRAINING_NAME)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_TRAINER, TRAINER_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainingModel.class)
                .value(this::checkTrainingModelAssertions);
    }
}