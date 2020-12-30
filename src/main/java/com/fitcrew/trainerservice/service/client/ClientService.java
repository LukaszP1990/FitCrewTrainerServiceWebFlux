package com.fitcrew.trainerservice.service.client;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.core.converter.EmailConverter;
import com.fitcrew.trainerservice.core.converter.RatingConverter;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.core.util.RankingModelUtil;
import com.fitcrew.trainerservice.core.util.RatingTrainerDocumentUtil;
import com.fitcrew.trainerservice.dao.EmailRepository;
import com.fitcrew.trainerservice.dao.RatingTrainerRepository;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.RatingTrainerDocument;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientService {

    private final TrainerRepository trainerRepository;
    private final EmailRepository emailRepository;
    private final EmailConverter emailConverter;
    private final RatingTrainerRepository ratingTrainerRepository;
    private final RatingConverter ratingTrainerConverter;
    private final TrainerConverter trainerConverter;

    public ClientService(final TrainerRepository trainerRepository,
                         final EmailRepository emailRepository,
                         final EmailConverter emailConverter,
                         final RatingTrainerRepository ratingTrainerRepository,
                         final RatingConverter ratingTrainerConverter,
                         final TrainerConverter trainerConverter) {
        this.trainerRepository = trainerRepository;
        this.emailRepository = emailRepository;
        this.emailConverter = emailConverter;
        this.ratingTrainerRepository = ratingTrainerRepository;
        this.ratingTrainerConverter = ratingTrainerConverter;
        this.trainerConverter = trainerConverter;
    }

    Mono<EmailModel> sendMessageToTheTrainer(EmailModel email) {
        var firstAndLastNameOfRecipient = getRecipientFirstAndLastName(email);
        log.info("Send messageTo the trainer : {}", firstAndLastNameOfRecipient);
        return trainerRepository.findAll()
                .collectList()
                .filter(trainerDocuments -> !trainerDocuments.isEmpty())
                .map(trainerDocuments -> getTrainerDocument(firstAndLastNameOfRecipient, trainerDocuments))
                .flatMap(Mono::justOrEmpty)
                .map(trainerDocument -> emailConverter.emailModelToEmailDocument(email))
                .flatMap(emailRepository::save)
                .map(emailConverter::emailDocumentToEmailModel);
    }

    Mono<List<RankingModel>> getRankingOfTrainers() {
        log.info("Get trainers by ranking");
        return trainerRepository.findAll()
                .collectList()
                .filter(trainers -> !trainers.isEmpty())
                .map(this::getRankingModels);
    }

    Mono<Double> getAverageRatingOfTrainer(String trainerEmail) {
        log.info("Get average rating of trainer: {}", trainerEmail);
        return Try.of(() -> trainerEmail)
                .filter(Objects::nonNull)
                .map(trainerRepository::findByEmail)
                .map(trainerDocumentMono -> trainerDocumentMono.filter(Objects::nonNull)
                        .flatMap(this::calculateAverageRating))
                .getOrNull();
    }

    Mono<RatingTrainerModel> rateTrainer(String trainerEmail,
                                         String ratingForTrainer) {
        log.info("Rating: {} for the trainer: {}", ratingForTrainer, trainerEmail);
        return Try.of(() -> trainerEmail)
                .filter(Objects::nonNull)
                .map(trainerRepository::findByEmail)
                .map(trainerDocumentMono -> trainerDocumentMono.filter(Objects::nonNull)
                        .map(trainerDocument -> RatingTrainerDocumentUtil.getRatingTrainerDocument(ratingForTrainer, trainerDocument))
                        .filter(Objects::nonNull)
                        .flatMap(ratingTrainerRepository::save)
                        .map(ratingTrainerConverter::ratingTrainerDocumentToRatingTrainerModel))
                .getOrNull();
    }

    Mono<List<TrainerModel>> getTrainers() {
        log.info("Get all trainers");
        return trainerRepository.findAll()
                .collectList()
                .filter(trainerDocuments -> !trainerDocuments.isEmpty())
                .map(this::getTrainerModels);
    }

    Mono<TrainerModel> getTrainer(String trainerEmail) {
        log.info("Get trainer by email: {}", trainerEmail);
        return Try.of(() -> trainerEmail)
                .filter(Objects::nonNull)
                .map(trainerRepository::findByEmail)
                .map(trainerDocumentMono -> trainerDocumentMono.filter(Objects::nonNull)
                        .map(trainerConverter::trainerDocumentToTrainerModel))
                .getOrNull();
    }

    private Tuple2<String, String> getRecipientFirstAndLastName(EmailModel emailModel) {
        return Optional.of(emailModel)
                .filter(email -> Objects.nonNull(email.getRecipient()))
                .map(email -> email.getRecipient().split(" "))
                .filter(recipient -> !recipient[0].isEmpty() && !recipient[1].isEmpty())
                .map(recipient -> Tuple.of(recipient[0], recipient[1]))
                .orElse(null);
    }

    private Optional<TrainerDocument> getTrainerDocument(Tuple2<String, String> recipient,
                                                         List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .filter(trainerDocument -> areFirstAndLastNameEquals(recipient, trainerDocument))
                .findFirst();
    }

    private boolean areFirstAndLastNameEquals(Tuple2<String, String> recipient,
                                              TrainerDocument trainerDocument) {
        return recipient._1.equals(trainerDocument.getFirstName()) && recipient._2.equals(trainerDocument.getLastName());
    }

    private List<TrainerModel> getTrainerModels(List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .map(trainerConverter::trainerDocumentToTrainerModel)
                .collect(Collectors.toList());
    }

    private List<RankingModel> getRankingModels(List<TrainerDocument> trainerDocuments) {
        return trainerDocuments.stream()
                .map(RankingModelUtil::getRankingModel)
                .sorted(Comparator.comparingInt(RankingModel::getPlaceInTheRanking))
                .collect(Collectors.toList());
    }

    private Mono<Double> calculateAverageRating(TrainerDocument trainerDocument) {
        var trainerId = Long.valueOf(trainerDocument.getId());
        return ratingTrainerRepository.findByTrainerId(trainerId)
                .collectList()
                .filter(ratingTrainerDocuments -> !ratingTrainerDocuments.isEmpty())
                .map(this::getAverage)
                .filter(OptionalDouble::isPresent)
                .map(OptionalDouble::getAsDouble);
    }

    private OptionalDouble getAverage(List<RatingTrainerDocument> ratingTrainerDocuments) {
        return ratingTrainerDocuments.stream()
                .mapToDouble(RatingTrainerDocument::getRating)
                .average();
    }
}
