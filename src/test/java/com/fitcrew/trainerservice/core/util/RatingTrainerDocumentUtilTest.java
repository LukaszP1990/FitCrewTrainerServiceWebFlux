package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;
import static org.junit.jupiter.api.Assertions.*;

class RatingTrainerDocumentUtilTest {

    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();

    @Test
    void shouldGetRatingTrainerDocument() {
        var ratingTrainerDocument = RatingTrainerDocumentUtil.getRatingTrainerDocument(String.valueOf(1), trainerDocument);
        assertNotNull(ratingTrainerDocument);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, ratingTrainerDocument.getFirstName());
            assertEquals(TRAINER_LAST_NAME, ratingTrainerDocument.getLastName());
            assertEquals(1, ratingTrainerDocument.getRating());
        });
    }
}