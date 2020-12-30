package com.fitcrew.trainerservice.util;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.trainerservice.dto.TrainingDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;

public class TrainingUtil {
    
    public static final String CLIENT = "client";
    public static final String DESCRIPTION = "description";
    public static final String TRAINING = "example of traning";
    public static final String TRAINING_NAME = "training name";

    public static TrainingDto getTrainingDto() {
        return TrainingDto.builder()
                .clients(Collections.singletonList(CLIENT))
                .trainerEmail(TRAINER_EMAIL)
                .description(DESCRIPTION)
                .training(TRAINING)
                .trainingName(TRAINING_NAME)
                .build();
    }

    public static List<TrainingModel> getTrainingModels() {
        return IntStream.rangeClosed(1,3)
                .mapToObj(value -> getTrainingModel())
                .collect(Collectors.toList());
    }

    public static TrainingModel getTrainingModel() {
        return TrainingModel.builder()
                .clients(Collections.singletonList(CLIENT))
                .trainerEmail(TRAINER_EMAIL)
                .description(DESCRIPTION)
                .training(TRAINING)
                .trainingName(TRAINING_NAME)
                .build();
    }
}
