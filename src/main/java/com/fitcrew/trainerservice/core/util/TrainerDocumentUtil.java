package com.fitcrew.trainerservice.core.util;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainerDocumentUtil {

	public static TrainerDocument getUpdatedTrainerDocument(TrainerDto trainerDto,
															TrainerDocument trainerDocument) {
		trainerDocument.setTrainerId(trainerDto.getTrainerId());
		trainerDocument.setDateOfBirth(trainerDto.getDateOfBirth());
		trainerDocument.setEncryptedPassword(trainerDto.getEncryptedPassword());
		trainerDocument.setFirstName(trainerDto.getFirstName());
		trainerDocument.setLastName(trainerDto.getLastName());
		trainerDocument.setPhone(trainerDto.getPhone());
		trainerDocument.setEmail(trainerDto.getEmail());
		trainerDocument.setPlaceInTheRanking(trainerDto.getPlaceInTheRanking());
		trainerDocument.setSomethingAboutYourself(trainerDto.getSomethingAboutYourself());

		return trainerDocument;
	}
}
