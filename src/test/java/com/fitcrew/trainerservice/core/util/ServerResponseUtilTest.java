package com.fitcrew.trainerservice.core.util;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

class ServerResponseUtilTest {

    private static final String MOCK = "mock";

    @Test
    void shouldReturnServerResponseWithStatusCode200() {
        StepVerifier.create(ServerResponseUtil.ok("mock"))
                .expectSubscription()
                .expectNextMatches(serverResponse -> HttpStatus.OK.equals(serverResponse.statusCode()))
                .verifyComplete();
    }

    @Test
    void shouldReturnServerResponseWithStatusCode400WithErrorType() {
        StepVerifier.create(ServerResponseUtil.badRequest(ErrorType.NO_ADMIN_FOUND))
                .expectSubscription()
                .expectNextMatches(serverResponse -> HttpStatus.BAD_REQUEST.equals(serverResponse.statusCode()))
                .verifyComplete();
    }

    @Test
    void shouldReturnServerResponseWithStatusCode400WithObject() {
        StepVerifier.create(ServerResponseUtil.badRequest(MOCK))
                .expectSubscription()
                .expectNextMatches(serverResponse -> HttpStatus.BAD_REQUEST.equals(serverResponse.statusCode()))
                .verifyComplete();
    }
}