package com.fitcrew.trainerservice.service.trainer;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainingDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.jwt.model.AuthenticationResponse;
import com.fitcrew.jwt.util.JWTUtil;
import com.fitcrew.jwt.util.PasswordEncoderUtil;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.core.converter.TrainingConverter;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.feignclient.FeignTrainingService;
import com.fitcrew.trainerservice.service.cache.AuthenticationRequestCache;
import com.fitcrew.trainerservice.service.cache.TrainerModelCacheService;
import io.vavr.Tuple;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class TrainerService {

    private final FeignTrainingService feignTrainingService;
    private final TrainerConverter trainerConverter;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final TrainingConverter trainingConverter;
    private final JWTUtil jwtUtil;
    private final AuthenticationRequestCache authenticationRequestCache;
    private final TrainerModelCacheService trainerModelCacheService;

    public TrainerService(final FeignTrainingService feignTrainingService,
                          final TrainerConverter trainerConverter,
                          final TrainerRepository trainerRepository,
                          final PasswordEncoderUtil passwordEncoderUtil,
                          final TrainingConverter trainingConverter,
                          final JWTUtil jwtUtil,
                          final AuthenticationRequestCache authenticationRequestCache,
                          final TrainerModelCacheService trainerModelCacheService) {
        this.feignTrainingService = feignTrainingService;
        this.trainerConverter = trainerConverter;
        this.trainerRepository = trainerRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
        this.trainingConverter = trainingConverter;
        this.jwtUtil = jwtUtil;
        this.authenticationRequestCache = authenticationRequestCache;
        this.trainerModelCacheService = trainerModelCacheService;
    }

    Mono<AuthenticationResponse> findByEmail(AuthenticationRequest authenticationRequest) {
        log.info("Try to login by email: {}", authenticationRequest.getEmail());
        return Mono.justOrEmpty(authenticationRequest)
                .filter(email -> Objects.nonNull(authenticationRequest.getEmail()))
                .flatMap(authenticationReq -> getAuthenticationResponse(authenticationRequest));
    }

    Mono<TrainerDocument> createTrainer(TrainerDto trainerDto) {
        log.info("Create trainer: {}", trainerDto);
        return Try.of(() -> trainerDto)
                .map(this::getTrainerDocument)
                .getOrNull();
    }

    Mono<TrainingModel> createTraining(TrainingDto trainerDto) {
        log.info("Create training: {}", trainerDto);
        return Try.of(() -> trainerDto)
                .filter(Objects::nonNull)
                .map(trainingConverter::trainingDtoToTrainingModel)
                .map(feignTrainingService::createTraining)
                .map(trainingModelMono -> trainingModelMono.filter(Objects::nonNull)
                        .map(trainingModel -> trainingModel))
                .getOrNull();
    }

    Mono<List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName) {
        log.info("Get clients who get from trainer training: {}", trainingName);
        return Try.of(() -> trainingName)
                .filter(Objects::nonNull)
                .map(feignTrainingService::clientsWhoBoughtTraining)
                .map(clientsFlux -> clientsFlux.collectList()
                        .filter(clients -> !clients.isEmpty())
                        .map(trainingModel -> trainingModel))
                .getOrNull();
    }

    Mono<List<TrainingModel>> getTrainerTrainings(String trainerEmail) {
        log.info("Get trainings trainer: {}", trainerEmail);
        return Try.of(() -> trainerEmail)
                .filter(Objects::nonNull)
                .map(feignTrainingService::getTrainerTrainings)
                .map(clientsFlux -> clientsFlux.collectList()
                        .filter(clients -> !clients.isEmpty())
                        .map(trainingModel -> trainingModel))
                .getOrNull();
    }

    Mono<TrainerModel> getBasicInformationAboutTrainer(String trainerEmail) {
        log.info("Get basic information about trainer: {}", trainerEmail);
        return Mono.justOrEmpty(trainerEmail)
                .filter(email -> Objects.nonNull(trainerEmail))
                .flatMap(email -> trainerModelCacheService.getTrainerModel(trainerEmail));
    }

    Mono<TrainingModel> updateTraining(TrainingDto trainingDto,
                                       String trainingName) {
        log.info("Update training: {}", trainingName);
        return Try.of(() -> Tuple.of(trainingDto, trainingName))
                .filter(trainingDtoTrainingNameTuple -> ObjectUtils.allNotNull(trainingDtoTrainingNameTuple._1, trainingDtoTrainingNameTuple._2))
                .map(trainingDtoTrainingNameTuple -> Tuple.of(trainingConverter.trainingDtoToTrainingModel(trainingDtoTrainingNameTuple._1), trainingDtoTrainingNameTuple._2))
                .map(trainerEmailTrainingNameTuple -> feignTrainingService.updateTraining(trainerEmailTrainingNameTuple._1, trainerEmailTrainingNameTuple._2))
                .map(trainingModelMono -> trainingModelMono.filter(Objects::nonNull)
                        .map(trainingModel -> trainingModel))
                .getOrNull();
    }

    Mono<TrainingModel> deleteTraining(String trainerEmail,
                                       String trainingName) {
        log.info("Delete training: {} created by: {}", trainingName, trainerEmail);
        return Try.of(() -> Tuple.of(trainerEmail, trainingName))
                .filter(trainerEmailTrainingNameTuple -> ObjectUtils.allNotNull(trainerEmailTrainingNameTuple._1, trainerEmailTrainingNameTuple._2))
                .map(trainerEmailTrainingNameTuple -> feignTrainingService.deleteTraining(trainerEmailTrainingNameTuple._1, trainerEmailTrainingNameTuple._2))
                .map(trainingModelMono -> trainingModelMono.filter(Objects::nonNull)
                        .map(trainingModel -> trainingModel))
                .getOrNull();
    }

    Mono<TrainingModel> selectTrainingToSend(String trainerEmail,
                                             String trainingName) {
        log.info("Select training training: {} to send", trainingName);
        return Try.of(() -> Tuple.of(trainerEmail, trainingName))
                .filter(trainerEmailTrainingNameTuple -> ObjectUtils.allNotNull(trainerEmailTrainingNameTuple._1, trainerEmailTrainingNameTuple._2))
                .map(trainerEmailTrainingNameTuple -> feignTrainingService.selectTraining(trainerEmailTrainingNameTuple._1, trainerEmailTrainingNameTuple._2))
                .map(trainingModelMono -> trainingModelMono.filter(Objects::nonNull)
                        .map(trainingModel -> trainingModel))
                .getOrNull();
    }

    Mono<TrainerModel> getTrainerByTrainerId(String trainerId) {
        log.info("Get trainer by trainer id: {}", trainerId);
        return Try.of(() -> trainerId)
                .map(id -> trainerRepository.getTrainerByTrainerId(trainerId))
                .map(trainerDocumentMono -> trainerDocumentMono.filter(Objects::nonNull)
                        .map(trainerConverter::trainerDocumentToTrainerModel))
                .getOrNull();
    }

    private Mono<AuthenticationResponse> getAuthenticationResponse(AuthenticationRequest authenticationRequest) {
        return authenticationRequestCache.get(authenticationRequest.getEmail())
                .flatMap(authReq -> getAuthenticationResponseFromCache(authReq, authenticationRequest))
                .switchIfEmpty(Mono.defer(() -> getAuthenticationResponseFromDb(authenticationRequest)));
    }

    private Mono<TrainerDocument> getTrainerDocument(TrainerDto trainerDto) {
        return trainerRepository.findByEmail(trainerDto.getEmail())
                .map(clientDocument -> TrainerDocument.builder().build())
                .switchIfEmpty(Mono.defer(() -> save(trainerDto)));
    }

    private Mono<AuthenticationResponse> getAuthenticationResponseFromCache(AuthenticationRequest authenticationRequestFromCache,
                                                                            AuthenticationRequest authenticationRequest) {
        return Mono.justOrEmpty(authenticationRequestFromCache)
                .filter(authenticationReqFromCache ->
                        passwordEncoderUtil.arePasswordsEqualByCache(authenticationReqFromCache.getPassword(), authenticationRequest.getPassword()))
                .map(authenticationReqFromCache -> new AuthenticationResponse(jwtUtil.generateToken(authenticationReqFromCache)));
    }

    private Mono<AuthenticationResponse> getAuthenticationResponseFromDb(AuthenticationRequest authenticationRequest) {
        return trainerRepository.findByEmail(authenticationRequest.getEmail())
                .filter(Objects::nonNull)
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .filter(trainerDocument -> passwordEncoderUtil.arePasswordsEqual(authenticationRequest.getPassword(), trainerDocument.getEncryptedPassword()))
                .map(trainerDocument -> new AuthenticationResponse(jwtUtil.generateToken(authenticationRequest)))
                .doOnSuccess(authenticationResponse -> authenticationRequestCache.put(authenticationRequest.getEmail(), authenticationRequest, 5));
    }

    private Mono<TrainerDocument> save(TrainerDto trainerDto) {
        setPredefinedData(trainerDto);
        return trainerRepository.save(trainerConverter.trainerDtoToTrainerDocument(trainerDto));
    }

    private void setPredefinedData(TrainerDto trainerDto) {
        trainerDto.setTrainerId(UUID.randomUUID().toString());
        trainerDto.setEncryptedPassword(passwordEncoderUtil.encode(trainerDto.getPassword()));
    }
}
