package com.fitcrew.trainerservice.util;

import com.fitcrew.trainerservice.domains.RatingTrainerDocument;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_FIRST_NAME;
import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_LAST_NAME;

public class RatingTrainerDocumentUtil {

    public static List<RatingTrainerDocument> getRatingTrainerDocuments() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(RatingTrainerDocumentUtil::getRatingTrainerDocument)
                .collect(Collectors.toList());
    }

    public static RatingTrainerDocument getRatingTrainerDocument(Integer rating) {
        return RatingTrainerDocument.builder()
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .rating(rating)
                .trainerId(1L)
                .build();
    }
}
