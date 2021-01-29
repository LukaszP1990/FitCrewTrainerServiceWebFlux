package com.fitcrew.trainerservice.dao;

import com.fitcrew.trainerservice.domains.RatingTrainerDocument;
import com.fitcrew.trainerservice.util.RatingTrainerDocumentUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;

@DataMongoTest
@ExtendWith(value = MockitoExtension.class)
@ActiveProfiles("test")
class RatingTrainerRepositoryTest {

    @Autowired
    private RatingTrainerRepository ratingTrainerRepository;

    @BeforeEach
    void setUp() {
        var ratingTrainerDocuments = Collections.singletonList(RatingTrainerDocumentUtil.getRatingTrainerDocument(1));
        ratingTrainerRepository.deleteAll().thenMany(Flux.fromIterable(ratingTrainerDocuments))
                .flatMap(ratingTrainerDocument -> ratingTrainerRepository.save(ratingTrainerDocument))
                .doOnNext(ratingTrainerDocument -> System.out.println("Inserted rating: " + ratingTrainerDocument))
                .blockLast();
    }

    @Test
    void findByTrainerId() {
        //when
        var result = ratingTrainerRepository.findByTrainerId(1L);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkRatingTrainerDocumentAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotFindByEmail() {
        //when
        var result = ratingTrainerRepository.findByTrainerId(2L);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    private boolean checkRatingTrainerDocumentAssertions(RatingTrainerDocument ratingTrainerDocument) {
        return TRAINER_FIRST_NAME.equals(ratingTrainerDocument.getFirstName()) &&
                TRAINER_LAST_NAME.equals(ratingTrainerDocument.getLastName()) &&
                1 == ratingTrainerDocument.getRating() &&
                1L == ratingTrainerDocument.getTrainerId();
    }
}