package com.fitcrew.trainerservice.service.handler;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import com.fitcrew.trainerservice.core.util.QueryParamUtil;
import com.fitcrew.trainerservice.core.util.ServerResponseUtil;
import com.fitcrew.trainerservice.dto.TrainingDto;
import com.fitcrew.trainerservice.service.trainer.TrainerServiceFacade;
import io.vavr.Tuple;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class TrainerHandler {

	private static final String TRAINING_NAME = "training-name";
	private static final String TRAINER_EMAIL = "trainer-email";

	private final TrainerServiceFacade trainerServiceFacade;

	public TrainerHandler(TrainerServiceFacade trainerServiceFacade) {
		this.trainerServiceFacade = trainerServiceFacade;
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> getClientsWhoGetTrainingFromTrainer(ServerRequest serverRequest) {
		var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
		return Mono.just(trainingName)
				.flatMap(trainerServiceFacade::getClientsWhoGetTrainingFromTrainer)
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_CLIENT_BOUGHT_TRAINING));
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> getBasicInformationAboutTrainer(ServerRequest serverRequest) {
		var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
		return Mono.just(trainerEmail)
				.flatMap(trainerServiceFacade::getBasicInformationAboutTrainer)
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_TRAINER_FOUND));
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> getTrainerTrainings(ServerRequest serverRequest) {
		var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
		return Mono.just(trainerEmail)
				.flatMap(trainerServiceFacade::getTrainerTrainings)
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_TRAININGS));
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> createTraining(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(TrainingDto.class)
				.flatMap(trainerServiceFacade::createTraining)
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_TRAINING_CREATED));
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> deleteTraining(ServerRequest serverRequest) {
		var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
		var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
		return Mono.just(Tuple.of(trainerEmail, trainingName))
				.filter(queryParams -> Objects.nonNull(queryParams._1) && Objects.nonNull(queryParams._2))
				.flatMap(queryParams -> trainerServiceFacade.deleteTraining(queryParams._1, queryParams._2))
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_TRAINING_DELETED));
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> updateTraining(ServerRequest serverRequest) {
		var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
		return serverRequest.bodyToMono(TrainingDto.class)
				.filter(trainingDto -> Objects.nonNull(trainingDto) && Objects.nonNull(trainingName))
				.map(trainingDto -> Tuple.of(trainingDto, trainingName))
				.flatMap(trainingDtoTrainingNameTuple -> trainerServiceFacade.updateTraining(trainingDtoTrainingNameTuple._1, trainingDtoTrainingNameTuple._2))
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_TRAINING_UPDATED));
	}

	@PreAuthorize("hasRole('TRAINER')")
	public Mono<ServerResponse> selectTrainingToSend(ServerRequest serverRequest) {
		var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
		var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
		return Mono.just(Tuple.of(trainerEmail, trainingName))
				.filter(queryParams -> Objects.nonNull(queryParams._1) && Objects.nonNull(queryParams._2))
				.flatMap(queryParams -> trainerServiceFacade.selectTrainingToSend(queryParams._1, queryParams._2))
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ErrorType.NO_TRAINING_SELECTED));
	}

	private Mono<ServerResponse> getBadRequest(ErrorType errorType) {
		return Mono.defer(() -> ServerResponseUtil.badRequest(errorType));
	}
}
