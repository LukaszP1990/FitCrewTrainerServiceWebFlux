package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class TrainerDocumentUtilTest {

    @Test
    void shouldGetUpdatedTrainerDocument() {
        //given
        var trainerDocument = TrainerUtil.getTrainerDocument();
        var trainerDto = TrainerUtil.getTrainerDto();

        //when
        var updatedTrainerDocument = TrainerDocumentUtil.getUpdatedTrainerDocument(trainerDto, trainerDocument);

        //then
        assertAll(() -> {
            assertNotNull(updatedTrainerDocument);
            assertEquals(String.valueOf(1), updatedTrainerDocument.getTrainerId());
            assertEquals(TRAINER_ENCRYPTED_PASSWORD, updatedTrainerDocument.getEncryptedPassword());
            assertEquals(TRAINER_FIRST_NAME, updatedTrainerDocument.getFirstName());
            assertEquals(TRAINER_LAST_NAME, updatedTrainerDocument.getLastName());
            assertEquals(TRAINER_DATE_OF_BIRTH, updatedTrainerDocument.getDateOfBirth());
            assertEquals(String.valueOf(1), updatedTrainerDocument.getPlaceInTheRanking());
            assertEquals(TRAINER_DESCRIPTION, updatedTrainerDocument.getSomethingAboutYourself());
            assertEquals(String.valueOf(1).concat(TRAINER_EMAIL), updatedTrainerDocument.getEmail());
            assertEquals(TRAINER_PHONE_NUMBER, updatedTrainerDocument.getPhone());
        });
    }
}