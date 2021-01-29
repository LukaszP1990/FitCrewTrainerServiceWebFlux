package com.fitcrew.trainerservice.service.handler;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.jwt.model.AuthenticationRequest;
import com.fitcrew.trainerservice.core.util.ServerResponseUtil;
import com.fitcrew.trainerservice.service.trainer.TrainerServiceFacade;
import com.fitcrew.validatorservice.core.error.model.ValidationAuthReqErrorDto;
import com.fitcrew.validatorservice.factory.ValidatorFactory;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthenticationHandler {

    private final TrainerServiceFacade trainerServiceFacade;
    private final ValidatorFactory validatorFactory;

    public AuthenticationHandler(final TrainerServiceFacade trainerServiceFacade,
                                 final ValidatorFactory validatorFactory) {
        this.trainerServiceFacade = trainerServiceFacade;
        this.validatorFactory = validatorFactory;
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

    private Mono<ServerResponse> getServerResponse(AuthenticationRequest authenticationRequest) {
        return validatorFactory.validate(authenticationRequest)
                .flatMap(authRequestValidation -> isValid(authRequestValidation, authenticationRequest));
    }

    private Mono<ServerResponse> isValid(Validation<Seq<ValidationAuthReqErrorDto>, AuthenticationRequest> authenticationRequests,
                                         AuthenticationRequest authenticationRequest) {
        return authenticationRequests.isValid() ?
                getServerResponseAfterAuthentication(authenticationRequest) :
                ServerResponseUtil.badRequest(authenticationRequests.getError());
    }

    private Mono<ServerResponse> getServerResponseAfterAuthentication(AuthenticationRequest authenticationRequest) {
        return trainerServiceFacade.login(authenticationRequest)
                .filter(Objects::nonNull)
                .flatMap(ServerResponseUtil::ok)
                .switchIfEmpty(getBadRequest(ServerResponseUtil.badRequest(ErrorType.LOGIN_ERROR)));
    }
}
