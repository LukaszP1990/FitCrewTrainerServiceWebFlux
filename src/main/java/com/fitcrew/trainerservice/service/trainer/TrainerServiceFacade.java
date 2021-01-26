package com.fitcrew.trainerservice.service.trainer;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainingDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.jwt.model.AuthenticationResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TrainerServiceFacade {

	Mono<AuthenticationResponse> login(AuthenticationRequest authenticationRequest);

	Mono<List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName);

	Mono<TrainerModel> getBasicInformationAboutTrainer(String trainerEmail);

	Mono<List<TrainingModel>> getTrainerTrainings(String trainerEmail);

	Mono<TrainingModel> createTraining(TrainingDto trainingDto);

	Mono<TrainingModel> deleteTraining(String trainerEmail,
									   String trainingName);

	Mono<TrainingModel> updateTraining(TrainingDto trainingDto,
									   String trainingName);

	Mono<TrainingModel> selectTrainingToSend(String trainerEmail, String trainingName);

	Mono<TrainerModel> createTrainer(TrainerDto trainerDto);
}
