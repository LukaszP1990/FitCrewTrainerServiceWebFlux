package com.fitcrew.trainerservice.resource.trainer;

import com.fitcrew.trainerservice.service.handler.AuthenticationHandler;
import com.fitcrew.trainerservice.service.handler.TrainerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class TrainerRouting {

	private static final String PATH = "/api/trainer";
	private static final String LOGIN = "/login";
	private static final String SIGN_UP = "/sign-up";
	private static final String BASIC_INFO_TRAINER = "/basic/info";
	private static final String TRAINING_CREATE = "/create/training";
	private static final String TRAINING_UPDATE = "/update/training";
	private static final String TRAINING_DELETE = "/delete/training";
	private static final String TRAINING_SELECT = "/select/training";
	private static final String TRAININGS_BY_CLIENT = "/trainings/bought";
	private static final String TRAININGS_BY_TRAINER = "/trainings";

	@Bean
	public RouterFunction<ServerResponse> trainerRoutes(TrainerHandler handler) {
		return RouterFunctions
				.route(GET(PATH.concat(BASIC_INFO_TRAINER)), handler::getBasicInformationAboutTrainer)
				.andRoute(GET(PATH.concat(TRAININGS_BY_TRAINER)), handler::getTrainerTrainings)
				.andRoute(POST(PATH.concat(TRAINING_CREATE)), handler::createTraining)
				.andRoute(PUT(PATH.concat(TRAINING_UPDATE)), handler::updateTraining)
				.andRoute(DELETE(PATH.concat(TRAINING_DELETE)), handler::deleteTraining)
				.andRoute(GET(PATH.concat(TRAINING_SELECT)), handler::selectTrainingToSend)
				.andRoute(GET(PATH.concat(TRAININGS_BY_CLIENT)), handler::getClientsWhoGetTrainingFromTrainer);
	}

	@Bean
	public RouterFunction<ServerResponse> authRoutes(AuthenticationHandler handler) {
		return RouterFunctions
				.route(POST(PATH.concat(LOGIN)), handler::login)
				.andRoute(POST(PATH.concat(SIGN_UP)), handler::signUpTrainer);
	}

}
