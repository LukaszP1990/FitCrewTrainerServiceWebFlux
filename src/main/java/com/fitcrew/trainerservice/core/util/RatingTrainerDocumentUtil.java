package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.domains.RatingTrainerDocument;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingTrainerDocumentUtil {

    public static RatingTrainerDocument getRatingTrainerDocument(String ratingForTrainer,
                                                                 TrainerDocument trainerDocument) {
        return RatingTrainerDocument.builder()
                .firstName(trainerDocument.getFirstName())
                .lastName(trainerDocument.getLastName())
                .rating(Integer.parseInt(ratingForTrainer))
                .trainerId(Long.valueOf(trainerDocument.getId()))
                .build();
    }
}
