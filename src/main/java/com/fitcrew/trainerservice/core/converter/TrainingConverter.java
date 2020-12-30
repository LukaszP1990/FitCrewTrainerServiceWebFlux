package com.fitcrew.trainerservice.core.converter;

import org.mapstruct.Mapper;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.trainerservice.dto.TrainingDto;

@Mapper
public interface TrainingConverter {
	TrainingModel trainingDtoToTrainingModel(TrainingDto trainingDto);
}
