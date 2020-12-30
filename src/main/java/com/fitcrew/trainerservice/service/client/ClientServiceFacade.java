package com.fitcrew.trainerservice.service.client;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClientServiceFacade {

	Mono<EmailModel> sendMessageToTheTrainer(EmailModel email);

	Mono<List<RankingModel>> getRankingOfTrainers();

	Mono<Double> getAverageRatingOfTrainer(String trainerEmail);

	Mono<RatingTrainerModel> rateTrainer(String trainerEmail,
										 String ratingForTrainer);

	Mono<List<TrainerModel>> getTrainers();

	Mono<TrainerModel> getTrainer(String trainerEmail);
}
