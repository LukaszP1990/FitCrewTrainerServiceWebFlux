package com.fitcrew.trainerservice.service.handler;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.trainerservice.core.util.ServerResponseUtil;
import com.fitcrew.trainerservice.service.trainer.TrainerServiceFacade;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthenticationHandler {

	private final TrainerServiceFacade trainerServiceFacade;

	public AuthenticationHandler(TrainerServiceFacade trainerServiceFacade) {
		this.trainerServiceFacade = trainerServiceFacade;
	}

	public Mono<ServerResponse> login(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(AuthenticationRequest.class)
				.flatMap(trainerServiceFacade::login)
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ServerResponseUtil.badRequest(ErrorType.LOGIN_ERROR)));
	}

	public Mono<ServerResponse> signUpTrainer(ServerRequest serverRequest) {
		return serverRequest
				.bodyToMono(TrainerDto.class)
				.flatMap(trainerServiceFacade::createTrainer)
				.filter(Objects::nonNull)
				.flatMap(ServerResponseUtil::ok)
				.switchIfEmpty(getBadRequest(ServerResponseUtil.badRequest(ErrorType.NO_TRAINER_SAVED)));
	}

	private Mono<ServerResponse> getBadRequest(Mono<ServerResponse> serverResponseMono) {
		return Mono.defer(() -> serverResponseMono);
	}
}
