package com.fitcrew.trainerservice.service.admin;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AdminServiceFacade {

	Mono<List<TrainerModel>> getTrainers();

	Mono<TrainerModel> deleteTrainer(String trainerEmail);

	Mono<TrainerModel> updateTrainer(TrainerDto trainerDto, String trainerEmail);

	Mono<TrainerModel> getTrainer(String trainerEmail);
}
