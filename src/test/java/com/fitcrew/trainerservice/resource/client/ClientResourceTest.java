package com.fitcrew.trainerservice.resource.client;

import com.fitcrew.FitCrewAppConstant.message.type.RoleType;
import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.AbstractIntegrationTest;
import com.fitcrew.trainerservice.util.EmailUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientResourceTest extends AbstractIntegrationTest {

    public static final String CLIENT_EMAIL = "mockedClient@gmail.com";
    private static final EmailModel emailModel = EmailUtil.getEmailModel();
    private static final String trainerEmail = String.valueOf(1).concat(TRAINER_EMAIL);

    @Test
    void shouldSendMessageToTheTrainer() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/message/send")
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_CLIENT, CLIENT_EMAIL))
                .body(Mono.just(emailModel), EmailModel.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmailModel.class)
                .value(this::checkEmailModelAssertions);
    }

    @Test
    void shouldGetRankingOfTrainers() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/ranking/trainers")
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_CLIENT, CLIENT_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RankingModel.class)
                .hasSize(3)
                .consumeWith(response ->
                        response.getResponseBody()
                                .forEach(rankingModel ->
                                        assertEquals(TRAINER_FIRST_NAME, rankingModel.getTrainerFirstName()))
                );
    }

    @Test
    void shouldRateTrainer() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/rate")
                        .queryParam("trainer-email", trainerEmail)
                        .queryParam("rating", String.valueOf(10))
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_CLIENT, CLIENT_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(RatingTrainerModel.class)
                .value(this::checkRatingTrainerModelAssertions);
    }

    @Test
    void shouldGetAverageRatingOfTrainer() {
        setUpRating();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/rating")
                        .queryParam("trainer-email", trainerEmail)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_CLIENT, CLIENT_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Double.class)
                .value(avgRating -> assertEquals(2d, avgRating.doubleValue()));
    }

    @Test
    void shouldGetTrainer() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer")
                        .queryParam("trainer-email", trainerEmail)
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_CLIENT, CLIENT_EMAIL))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrainerModel.class)
                .value(trainerModel -> checkTrainerModelAssertions(trainerModel, 1));
    }

    @Test
    void shouldGetTrainers() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/trainer/trainers")
                        .build())
                .header(AUTHORIZATION_HEADER, getToken(RoleType.ROLE_CLIENT, CLIENT_EMAIL))
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
}