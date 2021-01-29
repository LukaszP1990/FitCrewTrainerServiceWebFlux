package com.fitcrew.trainerservice.dao;

import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;

@DataMongoTest
@ExtendWith(value = MockitoExtension.class)
@ActiveProfiles("test")
class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @BeforeEach
    void setUp() {
        trainerRepository.deleteAll().thenMany(Flux.fromIterable(TrainerUtil.getTrainerDocuments()))
                .flatMap(trainerDocument -> trainerRepository.save(trainerDocument))
                .doOnNext(trainerDocument -> System.out.println("Inserted trainer: " + trainerDocument))
                .blockLast();
    }

    @Test
    void shouldFindByEmail() {
        //given
        var email = String.valueOf(1).concat(TRAINER_EMAIL);

        //when
        var result = trainerRepository.findByEmail(email);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerDocumentAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotFindByEmail() {
        //when
        var result = trainerRepository.findByEmail(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    private boolean checkTrainerDocumentAssertions(TrainerDocument trainerDocument) {
        return String.valueOf(1).equals(trainerDocument.getTrainerId()) &&
                TRAINER_ENCRYPTED_PASSWORD.equals(trainerDocument.getEncryptedPassword()) &&
                TRAINER_FIRST_NAME.equals(trainerDocument.getFirstName()) &&
                String.valueOf(1).equals(trainerDocument.getPlaceInTheRanking()) &&
                TRAINER_LAST_NAME.equals(trainerDocument.getLastName()) &&
                TRAINER_DATE_OF_BIRTH.equals(trainerDocument.getDateOfBirth()) &&
                String.valueOf(1).concat(TRAINER_EMAIL).equals(trainerDocument.getEmail()) &&
                TRAINER_DESCRIPTION.equals(trainerDocument.getSomethingAboutYourself()) &&
                TRAINER_PHONE_NUMBER.equals(trainerDocument.getPhone()) &&
                Objects.nonNull(trainerDocument.getId());
    }
}