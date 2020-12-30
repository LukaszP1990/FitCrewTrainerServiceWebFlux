package com.fitcrew.trainerservice.service.client;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ClientServiceFacadeImpl implements ClientServiceFacade {

	private final ClientService clientService;

	public ClientServiceFacadeImpl(ClientService clientService) {
		this.clientService = clientService;
	}

	@Override
	public Mono<EmailModel> sendMessageToTheTrainer(EmailModel email) {
		return clientService.sendMessageToTheTrainer(email);
	}

	@Override
	public Mono<List<RankingModel>> getRankingOfTrainers() {
		return clientService.getRankingOfTrainers();
	}

	@Override
	public Mono<Double> getAverageRatingOfTrainer(String trainerEmail) {
		return clientService.getAverageRatingOfTrainer(trainerEmail);
	}

	@Override
	public Mono<RatingTrainerModel> rateTrainer(String trainerEmail,
												String ratingForTrainer) {
		return clientService.rateTrainer(trainerEmail, ratingForTrainer);
	}

	@Override
	public Mono<List<TrainerModel>> getTrainers() {
		return clientService.getTrainers();
	}

	@Override
	public Mono<TrainerModel> getTrainer(String trainerEmail) {
		return clientService.getTrainer(trainerEmail);
	}
}
