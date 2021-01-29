package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.trainerservice.util.TrainingUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static com.fitcrew.trainerservice.util.TrainingUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class TrainingConverterTest {

    private final TrainingConverter trainingConverter = Mappers.getMapper(TrainingConverter.class);

    @Test
    void shouldConvertTrainingDtoToTrainingModel() {
        //given
        var trainingDto = TrainingUtil.getTrainingDto();

        //when
        var trainingModel = trainingConverter.trainingDtoToTrainingModel(trainingDto);

        //then
        assertAll(() -> {
            assertNotNull(trainingModel);
            assertEquals(1, trainingModel.getClients().size());
            assertEquals(TRAINER_EMAIL, trainingModel.getTrainerEmail());
            assertEquals(DESCRIPTION, trainingModel.getDescription());
            assertEquals(TRAINING, trainingModel.getTraining());
            assertEquals(TRAINING_NAME, trainingModel.getTrainingName());
        });
    }
}