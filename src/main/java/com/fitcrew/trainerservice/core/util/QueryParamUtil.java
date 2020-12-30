package com.fitcrew.trainerservice.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParamUtil {

    public static String getQueryParamValue(ServerRequest request,
                                            String queryParamName) {
        return Optional.ofNullable(request)
                .map(req -> req.queryParam(queryParamName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);
    }
}
