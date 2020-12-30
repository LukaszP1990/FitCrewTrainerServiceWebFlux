package com.fitcrew.trainerservice.core.converter;

import org.mapstruct.Mapper;

import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.trainerservice.domains.RatingTrainerDocument;

@Mapper
public interface RatingConverter {
	RatingTrainerModel ratingTrainerDocumentToRatingTrainerModel(RatingTrainerDocument ratingTrainerDocument);
}
