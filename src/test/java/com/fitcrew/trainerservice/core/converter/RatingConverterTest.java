package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.trainerservice.util.RatingTrainerDocumentUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;
import static org.junit.jupiter.api.Assertions.*;

class RatingConverterTest {

    private final RatingConverter ratingConverter = Mappers.getMapper(RatingConverter.class);

    @Test
    void shouldConvertRatingTrainerDocumentToRatingTrainerModel() {
        //given
        var ratingTrainerDocument = RatingTrainerDocumentUtil.getRatingTrainerDocument(1);

        //when
        var ratingTrainerModel = ratingConverter.ratingTrainerDocumentToRatingTrainerModel(ratingTrainerDocument);

        //then
        assertAll(() -> {
            assertNotNull(ratingTrainerModel);
            assertEquals(TRAINER_FIRST_NAME, ratingTrainerModel.getFirstName());
            assertEquals(TRAINER_LAST_NAME, ratingTrainerModel.getLastName());
            assertEquals(1, ratingTrainerModel.getRating());
        });
    }
}