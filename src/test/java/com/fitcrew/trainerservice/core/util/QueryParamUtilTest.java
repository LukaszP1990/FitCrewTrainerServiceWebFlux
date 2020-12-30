package com.fitcrew.trainerservice.core.util;

import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;

import static org.junit.jupiter.api.Assertions.*;

class QueryParamUtilTest {

    private static final String TRAINER_EMAIL = "trainer-email";

    @Test
    void shouldGetQueryParamValue() {
        var queryParamValue = QueryParamUtil.getQueryParamValue(getServerRequest(), TRAINER_EMAIL);
        assertNotNull(queryParamValue);
        assertEquals(TrainerUtil.TRAINER_EMAIL, queryParamValue);
    }

    private MockServerRequest getServerRequest() {
        return MockServerRequest.builder()
                .queryParam(TRAINER_EMAIL, TrainerUtil.TRAINER_EMAIL)
                .build();
    }
}