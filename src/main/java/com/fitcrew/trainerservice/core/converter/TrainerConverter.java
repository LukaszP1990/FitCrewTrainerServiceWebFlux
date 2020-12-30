package com.fitcrew.trainerservice.core.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.dto.TrainerDto;

@Mapper
public interface TrainerConverter {

	@Mapping(target = "id", ignore = true)
	TrainerDocument trainerDtoToTrainerDocument(TrainerDto trainerDto);

	TrainerModel trainerDocumentToTrainerModel(TrainerDocument trainerDocument);
}
