package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;
import static org.junit.jupiter.api.Assertions.*;

class RatingTrainerDocumentUtilTest {

    @Test
    void shouldGetRatingTrainerDocument() {
        //given
        var trainerDocument = TrainerUtil.getTrainerDocument();

        //when
        var ratingTrainerDocument = RatingTrainerDocumentUtil.getRatingTrainerDocument(String.valueOf(1), trainerDocument);

        //then
        assertAll(() -> {
            assertNotNull(ratingTrainerDocument);
            assertEquals(TRAINER_FIRST_NAME, ratingTrainerDocument.getFirstName());
            assertEquals(TRAINER_LAST_NAME, ratingTrainerDocument.getLastName());
            assertEquals(1, ratingTrainerDocument.getRating());
        });
    }
}