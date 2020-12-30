package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.trainerservice.dto.TrainingDto;
import com.fitcrew.trainerservice.util.TrainingUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static com.fitcrew.trainerservice.util.TrainingUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class TrainingConverterTest {

    private static final TrainingDto trainingDto = TrainingUtil.getTrainingDto();
    private final TrainingConverter trainingConverter = Mappers.getMapper(TrainingConverter.class);

    @Test
    void shouldConvertTrainingDtoToTrainingModel() {
        var trainingModel = trainingConverter.trainingDtoToTrainingModel(trainingDto);
        assertNotNull(trainingModel);
        assertAll(() -> {
            assertEquals(1, trainingModel.getClients().size());
            assertEquals(TRAINER_EMAIL, trainingModel.getTrainerEmail());
            assertEquals(DESCRIPTION, trainingModel.getDescription());
            assertEquals(TRAINING, trainingModel.getTraining());
            assertEquals(TRAINING_NAME, trainingModel.getTrainingName());
        });
    }
}