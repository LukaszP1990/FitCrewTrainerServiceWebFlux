package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.trainerservice.domains.RatingTrainerDocument;
import com.fitcrew.trainerservice.util.RatingTrainerDocumentUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;
import static org.junit.jupiter.api.Assertions.*;

class RatingConverterTest {

    private static final RatingTrainerDocument ratingTrainerDocument = RatingTrainerDocumentUtil.getRatingTrainerDocument(1);
    private final RatingConverter ratingConverter = Mappers.getMapper(RatingConverter.class);

    @Test
    void shouldConvertRatingTrainerDocumentToRatingTrainerModel() {
        RatingTrainerModel ratingTrainerModel = ratingConverter.ratingTrainerDocumentToRatingTrainerModel(ratingTrainerDocument);
        assertNotNull(ratingTrainerModel);
        assertAll(() -> {
            assertEquals(TRAINER_FIRST_NAME, ratingTrainerModel.getFirstName());
            assertEquals(TRAINER_LAST_NAME, ratingTrainerModel.getLastName());
            assertEquals(1, ratingTrainerModel.getRating());
        });
    }
}