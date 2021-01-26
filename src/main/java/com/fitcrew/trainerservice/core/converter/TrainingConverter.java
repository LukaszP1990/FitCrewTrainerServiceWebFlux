package com.fitcrew.trainerservice.core.converter;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainingDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import org.mapstruct.Mapper;

@Mapper
public interface TrainingConverter {
	TrainingModel trainingDtoToTrainingModel(TrainingDto trainingDto);
}
