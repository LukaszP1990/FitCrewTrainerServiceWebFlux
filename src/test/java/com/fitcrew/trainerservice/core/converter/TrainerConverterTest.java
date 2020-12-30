package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.dto.TrainerDto;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class TrainerConverterTest {

    private static final TrainerDto trainerDto = TrainerUtil.getTrainerDto();
    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();
    private final TrainerConverter trainerConverter = Mappers.getMapper(TrainerConverter.class);

    @Test
    void shouldConvertTrainerDtoToTrainerDocument() {
        var trainerDocument = trainerConverter.trainerDtoToTrainerDocument(trainerDto);
        assertNotNull(trainerDocument);
        checkTrainerDocumentAssertions(trainerDocument);
    }

    @Test
    void shouldConvertTrainerDocumentToTrainerModel() {
        var trainerModel = trainerConverter.trainerDocumentToTrainerModel(trainerDocument);
        assertNotNull(trainerModel);
        checkTrainerModelAssertions(trainerModel);
    }

    private void checkTrainerDocumentAssertions(TrainerDocument trainerDocument) {
        assertAll(() -> {
            assertEquals(String.valueOf(1), trainerDocument.getTrainerId());
            assertEquals(TRAINER_ENCRYPTED_PASSWORD, trainerDocument.getEncryptedPassword());
            assertEquals(TRAINER_FIRST_NAME, trainerDocument.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerDocument.getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerDocument.getDateOfBirth());
            assertEquals(String.valueOf(1), trainerDocument.getPlaceInTheRanking());
            assertEquals(TRAINER_DESCRIPTION, trainerDocument.getSomethingAboutYourself());
            assertEquals(String.valueOf(1).concat(TRAINER_EMAIL), trainerDocument.getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, trainerDocument.getPhone());
        });
    }

    private void checkTrainerModelAssertions(TrainerModel trainerModel) {
        assertAll(() -> {
            assertEquals(String.valueOf(1), trainerModel.getTrainerId());
            assertEquals(TRAINER_ENCRYPTED_PASSWORD, trainerModel.getEncryptedPassword());
            assertEquals(TRAINER_FIRST_NAME, trainerModel.getFirstName());
            assertEquals(TRAINER_LAST_NAME, trainerModel.getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, trainerModel.getDateOfBirth());
            assertEquals(String.valueOf(1), trainerModel.getPlaceInTheRanking());
            assertEquals(TRAINER_DESCRIPTION, trainerModel.getSomethingAboutYourself());
            assertEquals(String.valueOf(1).concat(TRAINER_EMAIL), trainerModel.getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, trainerModel.getPhone());
        });
    }
}