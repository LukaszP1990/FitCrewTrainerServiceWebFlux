package com.fitcrew.trainerservice;

import com.fitcrew.FitCrewAppConstant.message.type.RoleType;
import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.trainerservice.dao.RatingTrainerRepository;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.util.JwtUtil;
import com.fitcrew.trainerservice.util.RatingTrainerDocumentUtil;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static com.fitcrew.trainerservice.util.EmailUtil.*;
import static com.fitcrew.trainerservice.util.EmailUtil.BODY_OF_MESSAGE;
import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static com.fitcrew.trainerservice.util.TrainingUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TrainerServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = "200000000")
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String SECRET = "ThisIsSecretForJWTHS256SignatureAlgorithmThatMUSTHave64ByteLength";
    private static final String PASSWORD = "rmkFOU0k/LsmSG0CrVmqk+9BitPoqVAavuH1+8mreh0=";

    @Autowired
    public WebTestClient webTestClient;

    @Autowired
    public TrainerRepository trainerRepository;

    @Autowired
    public RatingTrainerRepository ratingTrainerRepository;

    @BeforeEach
    void setUp() {
        trainerRepository.deleteAll().thenMany(Flux.fromIterable(TrainerUtil.getTrainerDocuments()))
                .flatMap(trainerDocument -> trainerRepository.save(trainerDocument))
                .doOnNext(trainerDocument -> System.out.println("Inserted trainer: " + trainerDocument))
                .blockLast();
    }

    public String getToken(RoleType roleType,
                           String email) {
        return JwtUtil.createToken(SECRET, roleType, email);
    }

    public void setUpRating() {
        ratingTrainerRepository.deleteAll().thenMany(Flux.fromIterable(RatingTrainerDocumentUtil.getRatingTrainerDocuments()))
                .flatMap(ratingTrainerDocument -> ratingTrainerRepository.save(ratingTrainerDocument))
                .doOnNext(trainerDocument -> System.out.println("Inserted ratingTrainer: " + trainerDocument))
                .blockLast();
    }

    public void checkTrainerModelAssertions(TrainerModel trainerModel,
                                            int trainerId) {
        assertAll(() -> {
            assertEquals(String.valueOf(trainerId), trainerModel.getTrainerId());
            assertEquals(TRAINER_ENCRYPTED_PASSWORD, trainerModel.getEncryptedPassword());
            assertEquals(TRAINER_FIRST_NAME, trainerModel.getFirstName());
            assertEquals(String.valueOf(1), trainerModel.getPlaceInTheRanking());
            assertEquals(TRAINER_LAST_NAME, trainerModel.getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerModel.getDateOfBirth());
            assertEquals(String.valueOf(1).concat(TRAINER_EMAIL), trainerModel.getEmail());
            assertEquals(TRAINER_DESCRIPTION, trainerModel.getSomethingAboutYourself());
            assertEquals(TRAINER_PHONE_NUMBER, trainerModel.getPhone());
        });
    }

    public void checkTrainingModelAssertions(TrainingModel trainingModel) {
        assertAll(() -> {
            assertEquals(1, (trainingModel.getClients().size()));
            assertEquals(TRAINER_EMAIL, trainingModel.getTrainerEmail());
            assertEquals(DESCRIPTION, trainingModel.getDescription());
            assertEquals(TRAINING, trainingModel.getTraining());
            assertEquals(TRAINING_NAME, trainingModel.getTrainingName());
        });
    }

    public void checkEmailModelAssertions(EmailModel emailModel) {
        assertAll(() -> {
            assertEquals(SENDER, emailModel.getSender());
            assertEquals(RECIPIENT, emailModel.getRecipient());
            assertEquals(SUBJECT, emailModel.getSubject());
            assertEquals(BODY_OF_MESSAGE, emailModel.getBodyOfMessage());
        });
    }

    public void checkRatingTrainerModelAssertions(RatingTrainerModel ratingTrainerModel) {
        assertNotNull(ratingTrainerModel);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, ratingTrainerModel.getFirstName());
            assertEquals(TRAINER_LAST_NAME, ratingTrainerModel.getLastName());
            assertEquals(10, ratingTrainerModel.getRating());
        });
    }

    public AuthenticationRequest getAuthenticationRequest() {
        return new AuthenticationRequest(String.valueOf(1).concat(TRAINER_EMAIL), PASSWORD, RoleType.ROLE_TRAINER);
    }
}
