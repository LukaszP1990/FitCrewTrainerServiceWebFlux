package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.dto.TrainerDto;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainerDocumentUtilTest {

    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();
    private static final TrainerDto trainerDto = TrainerUtil.getTrainerDto();

    @Test
    void shouldGetUpdatedTrainerDocument() {
        var updatedTrainerDocument = TrainerDocumentUtil.getUpdatedTrainerDocument(trainerDto, trainerDocument);
        assertNotNull(updatedTrainerDocument);
        assertAll(() -> {
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