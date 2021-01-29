package com.fitcrew.trainerservice.service.client;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.core.converter.*;
import com.fitcrew.trainerservice.dao.EmailRepository;
import com.fitcrew.trainerservice.dao.RatingTrainerRepository;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.EmailDocument;
import com.fitcrew.trainerservice.domains.RatingTrainerDocument;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.util.EmailUtil;
import com.fitcrew.trainerservice.util.RatingTrainerDocumentUtil;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.fitcrew.trainerservice.util.EmailUtil.*;
import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClientServiceTest {

    private static final List<TrainerDocument> trainerDocuments = TrainerUtil.getTrainerDocuments();
    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();
    private static final EmailDocument emailDocument = EmailUtil.getEmailDocument();
    private static final EmailModel emailModel = EmailUtil.getEmailModel();
    private static final List<RatingTrainerDocument> ratingTrainerDocuments = RatingTrainerDocumentUtil.getRatingTrainerDocuments();
    private static final RatingTrainerDocument ratingTrainerDocument = RatingTrainerDocumentUtil.getRatingTrainerDocument(1);

    private final TrainerRepository trainerRepository = mock(TrainerRepository.class);
    private final EmailRepository emailRepository = mock(EmailRepository.class);
    private final RatingTrainerRepository ratingTrainerRepository = mock(RatingTrainerRepository.class);
    private final EmailConverter emailConverter = new EmailConverterImpl();
    private final RatingConverter ratingConverter = new RatingConverterImpl();
    private final TrainerConverter trainerConverter = new TrainerConverterImpl();
    private final ClientService clientService = new ClientService(
            trainerRepository,
            emailRepository,
            emailConverter,
            ratingTrainerRepository,
            ratingConverter,
            trainerConverter);

    @Test
    void shouldSendMessageToTheTrainer() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.fromIterable(trainerDocuments));
        when(emailRepository.save(any()))
                .thenReturn(Mono.just(emailDocument));

        //when
        var result = clientService.sendMessageToTheTrainer(emailModel);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkEmailModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotSendMessageToTheTrainerWhenNoTrainerExistInDatabase() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.empty());

        //when
        var result = clientService.sendMessageToTheTrainer(emailModel);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldNotSendMessageToTheTrainerWhenFirstAndLastNameNotMatchWithAnyTrainerInDatabase() {
        //given
        var updatedEmailModel = EmailModel.builder()
                .bodyOfMessage(BODY_OF_MESSAGE)
                .recipient(RECIPIENT.concat("1"))
                .sender(SENDER)
                .build();
        when(trainerRepository.findAll())
                .thenReturn(Flux.fromIterable(trainerDocuments));

        //when
        var result = clientService.sendMessageToTheTrainer(updatedEmailModel);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetRankingOfTrainers() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.fromIterable(trainerDocuments));

        //when
        var result = clientService.getRankingOfTrainers();

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(ranking -> 3 == ranking.size())
                .verifyComplete();
    }

    @Test
    void shouldNotGetRankingOfTrainers() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.empty());

        //when
        var result = clientService.getRankingOfTrainers();

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetAverageRatingOfTrainer() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        when(ratingTrainerRepository.findByTrainerId(anyLong()))
                .thenReturn(Flux.fromIterable(ratingTrainerDocuments));

        //when
        var result = clientService.getAverageRatingOfTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(avgRating -> 2d == avgRating)
                .verifyComplete();
    }

    @Test
    void shouldNotGetAverageRatingOfTrainerWhenRatingForTrainerNotExists() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        when(ratingTrainerRepository.findByTrainerId(anyLong()))
                .thenReturn(Flux.empty());

        //when
        var result = clientService.getAverageRatingOfTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldRateTrainer() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        when(ratingTrainerRepository.save(any()))
                .thenReturn(Mono.just(ratingTrainerDocument));

        //when
        var result = clientService.rateTrainer(TRAINER_EMAIL, String.valueOf(10));

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkRatingTrainerDocumentAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotRateTrainerWhenWhenNoTrainerExistInDatabase() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());

        //when
        var result = clientService.rateTrainer(TRAINER_EMAIL, String.valueOf(10));

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetTrainers() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.fromIterable(trainerDocuments));

        //when
        var result = clientService.getTrainers();

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(trainers -> 3 == trainers.size())
                .verifyComplete();
    }

    @Test
    void shouldNotGetTrainers() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.empty());

        //when
        var result = clientService.getTrainers();

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldGetTrainer() {
        //given
        when(trainerRepository.findByEmail(TRAINER_EMAIL))
                .thenReturn(Mono.just(trainerDocument));

        //when
        var result = clientService.getTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotGetTrainer() {
        //given
        when(trainerRepository.findByEmail(TRAINER_EMAIL))
                .thenReturn(Mono.empty());

        //when
        var result = clientService.getTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    private boolean checkEmailModelAssertions(EmailModel emailModel) {
        return SENDER.equals(emailModel.getSender()) &&
                RECIPIENT.equals(emailModel.getRecipient()) &&
                SUBJECT.equals(emailModel.getSubject()) &&
                BODY_OF_MESSAGE.equals(emailModel.getBodyOfMessage());
    }

    private boolean checkRatingTrainerDocumentAssertions(RatingTrainerModel ratingTrainerModel) {
        return TRAINER_FIRST_NAME.equals(ratingTrainerModel.getFirstName()) &&
                TRAINER_LAST_NAME.equals(ratingTrainerModel.getLastName()) &&
                1 == ratingTrainerModel.getRating();
    }

    private boolean checkTrainerModelAssertions(TrainerModel trainerModel) {
        return String.valueOf(1).equals(trainerModel.getTrainerId()) &&
                TRAINER_ENCRYPTED_PASSWORD.equals(trainerModel.getEncryptedPassword()) &&
                TRAINER_FIRST_NAME.equals(trainerModel.getFirstName()) &&
                String.valueOf(1).equals(trainerModel.getPlaceInTheRanking()) &&
                TRAINER_LAST_NAME.equals(trainerModel.getLastName()) &&
                TRAINER_DATE_OF_BIRTH.equals(trainerModel.getDateOfBirth()) &&
                String.valueOf(1).concat(TRAINER_EMAIL).equals(trainerModel.getEmail()) &&
                TRAINER_DESCRIPTION.equals(trainerModel.getSomethingAboutYourself()) &&
                TRAINER_PHONE_NUMBER.equals(trainerModel.getPhone());
    }
}