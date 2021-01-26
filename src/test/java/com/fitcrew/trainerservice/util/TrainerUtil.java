package com.fitcrew.trainerservice.util;

import com.fitcrew.FitCrewAppModel.domain.dto.RatingTrainerDto;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.domains.TrainerDocument;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainerUtil {

    public static final String TRAINER_EMAIL = "mockedTrainer@gmail.com";
    public static final String TRAINER_DATE_OF_BIRTH = "01.01.1990";
    public static final String TRAINER_PHONE_NUMBER = "501928341";
    public static final String TRAINER_DESCRIPTION = "Description about trainer";
    public static final String PASSWORD = "password";
    public static final String TRAINER_FIRST_NAME = "firstName";
    public static final String TRAINER_LAST_NAME = "lastName";
    public static final String TRAINER_ENCRYPTED_PASSWORD = "$2y$12$Y3QFw.tzF7OwIJGlpzk9s.5Ymq4zY3hItIkD0Xes3UWxBo2SkEgei";


    public static List<TrainerDocument> getTrainerDocuments() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(value -> prepareTrainerDocumentData(String.valueOf(value), value))
                .collect(Collectors.toList());
    }

    public static TrainerDocument getTrainerDocument() {
        return prepareTrainerDocumentData(String.valueOf(1), 1);
    }

    public static TrainerDto getTrainerDto() {
        return TrainerDto.builder()
                .trainerId(String.valueOf(1))
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .email(String.valueOf(1).concat(TRAINER_EMAIL))
                .dateOfBirth(TRAINER_DATE_OF_BIRTH)
                .phone(TRAINER_PHONE_NUMBER)
                .placeInTheRanking(String.valueOf(1))
                .somethingAboutYourself(TRAINER_DESCRIPTION)
                .password(PASSWORD)
                .encryptedPassword(TRAINER_ENCRYPTED_PASSWORD)
                .build();
    }

    public static TrainerModel getTrainerModel() {
        return prepareTrainerModelData(String.valueOf(1), 1);
    }


    private static TrainerDocument prepareTrainerDocumentData(String placeInTheRanking,
                                                              Integer value) {
        return TrainerDocument.builder()
                .id(String.valueOf(value))
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .email(String.valueOf(value).concat(TRAINER_EMAIL))
                .dateOfBirth(TRAINER_DATE_OF_BIRTH)
                .phone(TRAINER_PHONE_NUMBER)
                .placeInTheRanking(placeInTheRanking)
                .trainerId(String.valueOf(value))
                .somethingAboutYourself(TRAINER_DESCRIPTION)
                .encryptedPassword(TRAINER_ENCRYPTED_PASSWORD)
                .build();
    }

    private static TrainerModel prepareTrainerModelData(String placeInTheRanking,
                                                        Integer value) {
        return TrainerModel.builder()
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .email(String.valueOf(value).concat(TRAINER_EMAIL))
                .dateOfBirth(TRAINER_DATE_OF_BIRTH)
                .phone(TRAINER_PHONE_NUMBER)
                .placeInTheRanking(placeInTheRanking)
                .trainerId(String.valueOf(value))
                .somethingAboutYourself(TRAINER_DESCRIPTION)
                .encryptedPassword(TRAINER_ENCRYPTED_PASSWORD)
                .build();
    }
}
