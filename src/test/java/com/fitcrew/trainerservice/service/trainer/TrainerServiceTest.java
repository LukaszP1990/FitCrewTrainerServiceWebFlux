package com.fitcrew.trainerservice.service.trainer;

import com.fitcrew.FitCrewAppConstant.message.type.RoleType;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainingDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.jwt.util.JWTUtil;
import com.fitcrew.jwt.util.PasswordEncoderUtil;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.core.converter.TrainerConverterImpl;
import com.fitcrew.trainerservice.core.converter.TrainingConverter;
import com.fitcrew.trainerservice.core.converter.TrainingConverterImpl;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.feignclient.FeignTrainingService;
import com.fitcrew.trainerservice.service.cache.AuthenticationRequestCache;
import com.fitcrew.trainerservice.service.cache.TrainerModelCacheService;
import com.fitcrew.trainerservice.util.JwtUtil;
import com.fitcrew.trainerservice.util.TrainerUtil;
import com.fitcrew.trainerservice.util.TrainingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static com.fitcrew.trainerservice.util.TrainingUtil.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerServiceTest {
    protected static final String SECRET = "ThisIsSecretForJWTHS256SignatureAlgorithmThatMUSTHave64ByteLength";
    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();
    private static final TrainerDto trainerDto = TrainerUtil.getTrainerDto();
    private static final TrainerModel trainerModel = TrainerUtil.getTrainerModel();
    private static final TrainingModel trainingModel = TrainingUtil.getTrainingModel();
    private static final List<TrainingModel> trainingModels = TrainingUtil.getTrainingModels();
    private static final TrainingDto trainingDto = TrainingUtil.getTrainingDto();
    private static final List<String> clientsNames = Arrays.asList("firstName", "secondName");

    private final TrainerRepository trainerRepository = mock(TrainerRepository.class);
    private final FeignTrainingService feignTrainingService = mock(FeignTrainingService.class);
    private final PasswordEncoderUtil passwordEncoderUtil = mock(PasswordEncoderUtil.class);
    private final JWTUtil jwtUtil = mock(JWTUtil.class);
    private final AuthenticationRequestCache authenticationRequestCache = mock(AuthenticationRequestCache.class);
    private final TrainerModelCacheService trainerModelCacheService = mock(TrainerModelCacheService.class);
    private final TrainingConverter trainingConverter = new TrainingConverterImpl();
    private final TrainerConverter trainerConverter = new TrainerConverterImpl();
    private final TrainerService trainerService = new TrainerService(
            feignTrainingService,
            trainerConverter,
            trainerRepository,
            passwordEncoderUtil,
            trainingConverter,
            jwtUtil,
            authenticationRequestCache,
            trainerModelCacheService);

    @Test
    void shouldFindByEmailFromCache() {
        //given
        when(authenticationRequestCache.get(anyString()))
                .thenReturn(Mono.just(getAuthenticationRequest()));
        when(passwordEncoderUtil.arePasswordsEqualByCache(anyString(), anyString()))
                .thenReturn(true);
        when(jwtUtil.generateToken(any()))
                .thenReturn(JwtUtil.createToken(SECRET, RoleType.ROLE_TRAINER, TRAINER_EMAIL));

        //when
        var result = trainerService.findByEmail(getAuthenticationRequest());

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(authenticationResponse -> Objects.nonNull(authenticationResponse.getToken()))
                .verifyComplete();
    }

    @Test
    void shouldFindByEmailFromDatabase() {
        //given
        when(authenticationRequestCache.get(anyString()))
                .thenReturn(Mono.empty());
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        when(passwordEncoderUtil.arePasswordsEqual(anyString(), anyString()))
                .thenReturn(true);
        when(jwtUtil.generateToken(any()))
                .thenReturn(JwtUtil.createToken(SECRET, RoleType.ROLE_TRAINER, TRAINER_EMAIL));
        doNothing()
                .when(authenticationRequestCache).put(anyString(), any(), anyLong());

        //when
        var result = trainerService.findByEmail(getAuthenticationRequest());

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(authenticationResponse -> Objects.nonNull(authenticationResponse.getToken()))
                .verifyComplete();
    }

    @Test
    void shouldNotFindByEmailWhenTrainerIsNotInCacheAndInDatabase() {
        //given
        when(authenticationRequestCache.get(anyString()))
                .thenReturn(Mono.empty());
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());
        doNothing()
                .when(authenticationRequestCache).put(anyString(), any(), anyLong());

        //when
        var result = trainerService.findByEmail(getAuthenticationRequest());

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldCreateTrainer() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());
        when(trainerRepository.save(any()))
                .thenReturn(Mono.just(trainerDocument));

        //when
        var result = trainerService.createTrainer(trainerDto);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerDocumentAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotCreateTrainerWhenItAlreadyIsInDatabase() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));

        //when
        var result = trainerService.createTrainer(trainerDto);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(trainer -> Objects.isNull(trainer.getTrainerId()))
                .verifyComplete();
    }

    @Test
    void shouldCreateTraining() {
        //given
        when(feignTrainingService.createTraining(any()))
                .thenReturn(Mono.just(trainingModel));

        //when
        var result = trainerService.createTraining(trainingDto);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainingModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotCreateTraining() {
        //given
        when(feignTrainingService.createTraining(any()))
                .thenReturn(Mono.empty());

        //when
        var result = trainerService.createTraining(trainingDto);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetClientsWhoGetTrainingFromTrainer() {
        //given
        when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
                .thenReturn(Flux.fromIterable(clientsNames));

        //when
        var result = trainerService.getClientsWhoGetTrainingFromTrainer(TRAINING_NAME);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(clients -> 2 == clients.size())
                .verifyComplete();
    }

    @Test
    void shouldNotGetClientsWhoGetTrainingFromTrainer() {
        //given
        when(feignTrainingService.clientsWhoBoughtTraining(anyString()))
                .thenReturn(Flux.empty());

        //when
        var result = trainerService.getClientsWhoGetTrainingFromTrainer(TRAINING_NAME);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetTrainerTrainings() {
        //given
        when(feignTrainingService.getTrainerTrainings(anyString()))
                .thenReturn(Flux.fromIterable(trainingModels));

        //when
        var result = trainerService.getTrainerTrainings(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(trainings -> 3 == trainings.size())
                .verifyComplete();
    }

    @Test
    void shouldNotGetTrainerTrainings() {
        //given
        when(feignTrainingService.getTrainerTrainings(anyString()))
                .thenReturn(Flux.empty());

        //when
        var result = trainerService.getTrainerTrainings(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetBasicInformationAboutTrainer() {
        //given
        when(trainerModelCacheService.getTrainerModel(anyString()))
                .thenReturn(Mono.just(trainerModel));

        //when
        var result = trainerService.getBasicInformationAboutTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotUpdateTraining() {
        //given
        when(trainerModelCacheService.getTrainerModel(anyString()))
                .thenReturn(Mono.empty());

        //when
        var result = trainerService.getBasicInformationAboutTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldDeleteTraining() {
        //given
        when(feignTrainingService.deleteTraining(anyString(), anyString()))
                .thenReturn(Mono.just(trainingModel));

        //when
        var result = trainerService.deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainingModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotDeleteTraining() {
        //given
        when(feignTrainingService.deleteTraining(anyString(), anyString()))
                .thenReturn(Mono.empty());

        //when
        var result = trainerService.deleteTraining(TRAINER_EMAIL, TRAINING_NAME);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldSelectTrainingToSend() {
        //given
        when(feignTrainingService.selectTraining(anyString(), anyString()))
                .thenReturn(Mono.just(trainingModel));

        //when
        var result = trainerService.selectTrainingToSend(TRAINER_EMAIL, TRAINING_NAME);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainingModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotSelectTrainingToSend() {
        //given
        when(feignTrainingService.selectTraining(anyString(), anyString()))
                .thenReturn(Mono.empty());

        //when
        var result = trainerService.selectTrainingToSend(TRAINER_EMAIL, TRAINING_NAME);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    private AuthenticationRequest getAuthenticationRequest() {
        return new AuthenticationRequest(String.valueOf(1).concat(TRAINER_EMAIL), PASSWORD, RoleType.ROLE_TRAINER);
    }

    private boolean checkTrainerDocumentAssertions(TrainerDocument trainerDocument) {
        return String.valueOf(1).equals(trainerDocument.getTrainerId()) &&
                TRAINER_ENCRYPTED_PASSWORD.equals(trainerDocument.getEncryptedPassword()) &&
                TRAINER_FIRST_NAME.equals(trainerDocument.getFirstName()) &&
                TRAINER_LAST_NAME.equals(trainerDocument.getLastName()) &&
                TRAINER_DATE_OF_BIRTH.equals(trainerDocument.getDateOfBirth()) &&
                String.valueOf(1).equals(trainerDocument.getPlaceInTheRanking()) &&
                TRAINER_DESCRIPTION.equals(trainerDocument.getSomethingAboutYourself()) &&
                String.valueOf(1).concat(TRAINER_EMAIL).equals(trainerDocument.getEmail()) &&
                TRAINER_PHONE_NUMBER.equals(trainerDocument.getPhone());
    }

    private boolean checkTrainerModelAssertions(TrainerModel trainerModel) {
        return String.valueOf(1).equals(trainerModel.getTrainerId()) &&
                TRAINER_ENCRYPTED_PASSWORD.equals(trainerModel.getEncryptedPassword()) &&
                TRAINER_FIRST_NAME.equals(trainerModel.getFirstName()) &&
                TRAINER_LAST_NAME.equals(trainerModel.getLastName()) &&
                TRAINER_DATE_OF_BIRTH.equals(trainerModel.getDateOfBirth()) &&
                String.valueOf(1).equals(trainerModel.getPlaceInTheRanking()) &&
                TRAINER_DESCRIPTION.equals(trainerModel.getSomethingAboutYourself()) &&
                String.valueOf(1).concat(TRAINER_EMAIL).equals(trainerModel.getEmail()) &&
                TRAINER_PHONE_NUMBER.equals(trainerModel.getPhone());
    }

    private boolean checkTrainingModelAssertions(TrainingModel trainingModel) {
        return 1 == (trainingModel.getClients().size()) &&
                TRAINER_EMAIL.equals(trainingModel.getTrainerEmail()) &&
                DESCRIPTION.equals(trainingModel.getDescription()) &&
                TRAINING.equals(trainingModel.getTraining()) &&
                TRAINING_NAME.equals(trainingModel.getTrainingName());
    }
}