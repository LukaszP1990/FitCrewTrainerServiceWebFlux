package com.fitcrew.trainerservice.feignclient;

import com.fitcrew.FitCrewAppModel.domain.model.TrainingModel;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.fitcrew.trainerservice.core.constant.TrainingServiceConstant.TRAINING_RESOURCE;

@ReactiveFeignClient(name = TRAINING_RESOURCE)
public interface FeignTrainingService {

	@GetMapping(value = "/trainings")
	Flux<TrainingModel> getTrainerTrainings(@RequestParam(name = "trainer-email") String trainerEmail);

	@PostMapping
	Mono<TrainingModel> createTraining(@RequestBody TrainingModel trainingModel);

	@DeleteMapping
	Mono<TrainingModel> deleteTraining(@RequestParam(name = "trainer-email") String trainerEmail,
									   @RequestParam(name = "training-name") String trainingName);

	@PutMapping
	Mono<TrainingModel> updateTraining(@RequestBody TrainingModel trainingModel,
									   @RequestParam(name = "trainer-email") String trainerEmail);

	@GetMapping(value = "/select")
	Mono<TrainingModel> selectTraining(@RequestParam(name = "trainer-email") String trainerEmail,
									   @RequestParam(name = "training-name") String trainingName);

	@GetMapping(value = "/client-bought-training")
	Flux<String> clientsWhoBoughtTraining(@RequestParam(name = "training-name") String trainingName);
}
