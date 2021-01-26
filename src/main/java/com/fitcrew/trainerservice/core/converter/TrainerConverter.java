package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TrainerConverter {

	@Mapping(target = "id", ignore = true)
	TrainerDocument trainerDtoToTrainerDocument(TrainerDto trainerDto);

	TrainerModel trainerDocumentToTrainerModel(TrainerDocument trainerDocument);
}
