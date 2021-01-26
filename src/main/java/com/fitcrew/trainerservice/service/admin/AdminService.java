package com.fitcrew.trainerservice.service.admin;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.core.util.TrainerDocumentUtil;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.service.cache.TrainerModelCache;
import com.fitcrew.trainerservice.service.cache.TrainerModelCacheService;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
class AdminService {

    private final TrainerRepository trainerRepository;
    private final TrainerConverter trainerConverter;
    private final TrainerModelCache trainerModelCache;
    private final TrainerModelCacheService trainerModelCacheService;

    AdminService(final TrainerRepository trainerRepository,
                 final TrainerConverter trainerConverter,
                 final TrainerModelCache trainerModelCache,
                 final TrainerModelCacheService trainerModelCacheService) {
        this.trainerRepository = trainerRepository;
        this.trainerConverter = trainerConverter;
        this.trainerModelCache = trainerModelCache;
        this.trainerModelCacheService = trainerModelCacheService;
    }

    Mono<List<TrainerModel>> getTrainers() {
        log.info("Finding all trainers");
        return Try.of(trainerRepository::findAll)
                .map(trainerDocuments -> trainerDocuments
                        .collectList()
                        .filter(Objects::nonNull)
                        .map(this::convertToTrainerModel))
                .getOrNull();
    }

    Mono<TrainerModel> getTrainer(String trainerEmail) {
        log.info("Finding trainer by email: {}", trainerEmail);
        return Mono.justOrEmpty(trainerEmail)
                .filter(email -> Objects.nonNull(trainerEmail))
                .flatMap(email -> trainerModelCacheService.getTrainerModel(trainerEmail));
    }

    Mono<TrainerModel> updateTrainer(TrainerDto trainerDto,
                                     String trainerEmail) {
        log.info("Finding trainer to update by email: {}", trainerEmail);
        return Mono.justOrEmpty(trainerEmail)
                .filter(email -> Objects.nonNull(trainerEmail))
                .flatMap(trainerRepository::findByEmail)
                .filter(Objects::nonNull)
                .map(trainer -> TrainerDocumentUtil.getUpdatedTrainerDocument(trainerDto, trainer))
                .flatMap(trainerRepository::save)
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .doOnSuccess(trainerModel -> trainerModelCache.put(trainerEmail, trainerModel, 5));
    }

    Mono<TrainerModel> deleteTrainer(String trainerEmail) {
        log.info("Finding trainer to delete by email: {}", trainerEmail);
        return Mono.justOrEmpty(trainerEmail)
                .filter(email -> Objects.nonNull(trainerEmail))
                .flatMap(trainerRepository::findByEmail)
                .filter(Objects::nonNull)
                .flatMap(this::prepareSuccessfulTrainerDeleting)
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .doOnSuccess(clientModel -> trainerModelCache.delete(trainerEmail));
    }

    private Mono<TrainerDocument> prepareSuccessfulTrainerDeleting(TrainerDocument trainerToDelete) {
        trainerRepository.delete(trainerToDelete);
        return Mono.just(trainerToDelete);
    }

    private List<TrainerModel> convertToTrainerModel(List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .collect(Collectors.toList());
    }
}
