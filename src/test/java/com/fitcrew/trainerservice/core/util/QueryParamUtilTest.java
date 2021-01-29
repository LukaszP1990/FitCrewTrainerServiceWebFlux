package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;

import static org.junit.jupiter.api.Assertions.*;

class QueryParamUtilTest {

    private static final String TRAINER_EMAIL = "trainer-email";

    @Test
    void shouldGetQueryParamValue() {
        //given
        var serverRequest = getServerRequest();

        //when
        var queryParamValue = QueryParamUtil.getQueryParamValue(serverRequest, TRAINER_EMAIL);

        //then
        assertNotNull(queryParamValue);
        assertEquals(TrainerUtil.TRAINER_EMAIL, queryParamValue);
    }

    private MockServerRequest getServerRequest() {
        return MockServerRequest.builder()
                .queryParam(TRAINER_EMAIL, TrainerUtil.TRAINER_EMAIL)
                .build();
    }
}