package com.fitcrew.trainerservice.service.trainer;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.jwt.model.AuthenticationResponse;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.dto.TrainerDto;
import com.fitcrew.trainerservice.dto.TrainingDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class TrainerServiceFacadeImpl implements TrainerServiceFacade {

    private final TrainerService trainerService;
    private final TrainerConverter trainerConverter;

    public TrainerServiceFacadeImpl(final TrainerService trainerService,
                                    final TrainerConverter trainerConverter) {
        this.trainerService = trainerService;
        this.trainerConverter = trainerConverter;
    }

    @Override
    public Mono<AuthenticationResponse> login(AuthenticationRequest authenticationRequest) {
        return trainerService.findByEmail(authenticationRequest);
    }

    @Override
    public Mono<List<String>> getClientsWhoGetTrainingFromTrainer(String trainingName) {
        return trainerService.getClientsWhoGetTrainingFromTrainer(trainingName);
    }

    @Override
    public Mono<TrainerModel> getBasicInformationAboutTrainer(String trainerEmail) {
        return trainerService.getBasicInformationAboutTrainer(trainerEmail);
    }

    @Override
    public Mono<List<TrainingModel>> getTrainerTrainings(String trainerEmail) {
        return trainerService.getTrainerTrainings(trainerEmail);
    }

    @Override
    public Mono<TrainingModel> createTraining(TrainingDto trainingDto) {
        return trainerService.createTraining(trainingDto);
    }

    @Override
    public Mono<TrainingModel> deleteTraining(String trainerEmail,
                                              String trainingName) {
        return trainerService.deleteTraining(trainerEmail, trainingName);
    }

    @Override
    public Mono<TrainingModel> updateTraining(TrainingDto trainingDto,
                                              String trainingName) {
        return trainerService.updateTraining(trainingDto, trainingName);
    }

    @Override
    public Mono<TrainingModel> selectTrainingToSend(String trainerEmail,
                                                    String trainingName) {
        return trainerService.selectTrainingToSend(trainerEmail, trainingName);
    }

    @Override
    public Mono<TrainerModel> createTrainer(TrainerDto trainerDto) {
        return trainerService.createTrainer(trainerDto)
                .filter(trainerDocument -> Objects.nonNull(trainerDocument.getTrainerId()))
                .map(trainerConverter::trainerDocumentToTrainerModel);
    }
}
