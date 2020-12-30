package com.fitcrew.trainerservice.core.util;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ServerResponseUtilTest {

    @Test
    void shouldReturnServerResponseWithStatusCode200() {
        StepVerifier.create(ServerResponseUtil.ok("mock"))
                .expectSubscription()
                .expectNextMatches(serverResponse -> HttpStatus.OK.equals(serverResponse.statusCode()))
                .verifyComplete();
    }

    @Test
    void shouldReturnServerResponseWithStatusCode400() {
        StepVerifier.create(ServerResponseUtil.badRequest(ErrorType.NO_ADMIN_FOUND))
                .expectSubscription()
                .expectNextMatches(serverResponse -> HttpStatus.BAD_REQUEST.equals(serverResponse.statusCode()))
                .verifyComplete();
    }
}