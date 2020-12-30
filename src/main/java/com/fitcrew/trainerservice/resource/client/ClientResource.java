package com.fitcrew.trainerservice.resource.client;

import com.fitcrew.FitCrewAppModel.domain.model.EmailModel;
import com.fitcrew.FitCrewAppModel.domain.model.RankingModel;
import com.fitcrew.FitCrewAppModel.domain.model.RatingTrainerModel;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.service.client.ClientServiceFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/trainer")
public class ClientResource {

	private final ClientServiceFacade clientServiceFacade;

	public ClientResource(ClientServiceFacade adminServiceFacade) {
		this.clientServiceFacade = adminServiceFacade;
	}

	@PreAuthorize("hasRole('CLIENT')")
	@PostMapping(path = "/message/send", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<EmailModel> sendMessageToTheTrainer(@RequestBody EmailModel email) {
		return clientServiceFacade.sendMessageToTheTrainer(email);
	}

	@PreAuthorize("hasRole('CLIENT')")
	@GetMapping(path = "/ranking/trainers", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<RankingModel>> getRankingOfTrainers() {
		return clientServiceFacade.getRankingOfTrainers();
	}

	@PreAuthorize("hasRole('CLIENT')")
	@PostMapping(path = "/rate", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<RatingTrainerModel> rateTrainer(@RequestParam("trainer-email") String trainerEmail,
												@RequestParam("rating") String rate) {
		return clientServiceFacade.rateTrainer(trainerEmail, rate);
	}

	@PreAuthorize("hasRole('CLIENT')")
	@GetMapping(path = "/rating", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Double> getAverageRatingOfTrainer(@RequestParam("trainer-email") String trainerEmail) {
		return clientServiceFacade.getAverageRatingOfTrainer(trainerEmail);
	}

	@PreAuthorize("hasRole('CLIENT')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<TrainerModel> getTrainer(@RequestParam("trainer-email") String trainerEmail) {
		return clientServiceFacade.getTrainer(trainerEmail);
	}

	@PreAuthorize("hasRole('CLIENT')")
	@GetMapping(path = "/trainers", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<List<TrainerModel>> getTrainers() {
		return clientServiceFacade.getTrainers();
	}
}
