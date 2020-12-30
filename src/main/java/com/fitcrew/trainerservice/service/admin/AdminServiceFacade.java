package com.fitcrew.trainerservice.service.admin;

import java.util.List;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.dto.TrainerDto;

import reactor.core.publisher.Mono;

public interface AdminServiceFacade {

	Mono<List<TrainerModel>> getTrainers();

	Mono<TrainerModel> deleteTrainer(String trainerEmail);

	Mono<TrainerModel> updateTrainer(TrainerDto trainerDto, String trainerEmail);

	Mono<TrainerModel> getTrainer(String trainerEmail);
}
