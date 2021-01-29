package com.fitcrew.trainerservice.service.handler;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainingDto;
import com.fitcrew.trainerservice.core.util.QueryParamUtil;
import com.fitcrew.trainerservice.core.util.ServerResponseUtil;
import com.fitcrew.trainerservice.service.trainer.TrainerServiceFacade;
import com.fitcrew.validatorservice.core.error.model.ValidationTrainingErrorDto;
import com.fitcrew.validatorservice.factory.ValidatorFactory;
import io.vavr.Tuple;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.apache.commons.lang3.ObjectUtils;
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
    private static final String TRAINER_ID = "trainer-id";

    private final TrainerServiceFacade trainerServiceFacade;
    private final ValidatorFactory validatorFactory;

    public TrainerHandler(final TrainerServiceFacade trainerServiceFacade,
                          final ValidatorFactory validatorFactory) {
        this.trainerServiceFacade = trainerServiceFacade;
        this.validatorFactory = validatorFactory;
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> getClientsWhoGetTrainingFromTrainer(ServerRequest serverRequest) {
        var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
        return Mono.just(trainingName)
                .flatMap(trainerServiceFacade::getClientsWhoGetTrainingFromTrainer)
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_CLIENT_BOUGHT_TRAINING));
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> getBasicInformationAboutTrainer(ServerRequest serverRequest) {
        var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
        return Mono.just(trainerEmail)
                .flatMap(trainerServiceFacade::getBasicInformationAboutTrainer)
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAINER_FOUND));
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> getTrainerTrainings(ServerRequest serverRequest) {
        var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
        return Mono.just(trainerEmail)
                .flatMap(trainerServiceFacade::getTrainerTrainings)
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAININGS));
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> createTraining(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TrainingDto.class)
                .flatMap(this::getServerResponse);
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> deleteTraining(ServerRequest serverRequest) {
        var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
        var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
        return Mono.just(Tuple.of(trainerEmail, trainingName))
                .filter(queryParams -> ObjectUtils.allNotNull(queryParams._1, queryParams._2))
                .flatMap(queryParams -> trainerServiceFacade.deleteTraining(queryParams._1, queryParams._2))
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAINING_DELETED));
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> updateTraining(ServerRequest serverRequest) {
        var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
        return serverRequest.bodyToMono(TrainingDto.class)
                .flatMap(trainingDto -> getServerResponse(trainingDto, trainingName));
    }

    @PreAuthorize("hasRole('TRAINER')")
    public Mono<ServerResponse> selectTrainingToSend(ServerRequest serverRequest) {
        var trainerEmail = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);
        var trainingName = QueryParamUtil.getQueryParamValue(serverRequest, TRAINING_NAME);
        return Mono.just(Tuple.of(trainerEmail, trainingName))
                .filter(queryParams -> ObjectUtils.allNotNull(queryParams._1, queryParams._2))
                .flatMap(queryParams -> trainerServiceFacade.selectTrainingToSend(queryParams._1, queryParams._2))
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAINING_SELECTED));
    }

    public Mono<ServerResponse> getTrainerByTrainerId(ServerRequest serverRequest) {
        var trainerId = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_ID);
        return Mono.justOrEmpty(trainerId)
                .filter(Objects::nonNull)
                .flatMap(trainerServiceFacade::getTrainerByTrainerId)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAINER_BY_TRAINER_ID));
    }

    private Mono<ServerResponse> getServerResponse(TrainingDto trainingDto) {
        return validatorFactory.validate(trainingDto)
                .flatMap(trainerDtoValidation -> getServerResponseAfterCreate(trainerDtoValidation, trainingDto));
    }

    private Mono<ServerResponse> getServerResponse(TrainingDto trainingDto,
                                                   String trainingName) {
        return validatorFactory.validate(trainingDto)
                .flatMap(trainerDtoValidation -> getServerResponseAfterUpdate(trainerDtoValidation, trainingDto, trainingName));
    }

    private Mono<ServerResponse> getServerResponseAfterCreate(Validation<Seq<ValidationTrainingErrorDto>, TrainingDto> trainingDtoValidation,
                                                              TrainingDto trainingDto) {
        return trainingDtoValidation.isValid() ?
                getServerResponseAfterCreate(trainingDto) :
                ServerResponseUtil.badRequest(trainingDtoValidation.getError());
    }

    private Mono<ServerResponse> getServerResponseAfterUpdate(Validation<Seq<ValidationTrainingErrorDto>, TrainingDto> trainingDtoValidation,
                                                              TrainingDto trainingDto,
                                                              String trainingName) {
        return trainingDtoValidation.isValid() ?
                getServerResponseAfterUpdate(trainingDto, trainingName) :
                ServerResponseUtil.badRequest(trainingDtoValidation.getError());
    }

    private Mono<ServerResponse> getServerResponseAfterCreate(TrainingDto trainingDto) {
        return trainerServiceFacade.createTraining(trainingDto)
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAINING_CREATED));
    }

    private Mono<ServerResponse> getServerResponseAfterUpdate(TrainingDto trainingDto,
                                                              String trainingName) {
        return Mono.just(Tuple.of(trainingDto, trainingName))
                .flatMap(trainingDtoTrainingNameTuple -> trainerServiceFacade.updateTraining(trainingDtoTrainingNameTuple._1, trainingDtoTrainingNameTuple._2))
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(ServerResponseUtil.badRequest(ErrorType.NO_TRAINING_UPDATED));
    }


}
