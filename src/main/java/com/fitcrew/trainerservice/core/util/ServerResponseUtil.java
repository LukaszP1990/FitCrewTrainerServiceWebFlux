package com.fitcrew.trainerservice.core.util;

import com.fitcrew.FitCrewAppConstant.message.type.ErrorType;
import com.fitcrew.FitCrewAppModel.domain.dto.ErrorMsgDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerResponseUtil {

	public static <T> Mono<ServerResponse> ok(T body) {
		return ServerResponse
				.ok()
				.body(BodyInserters.fromValue(body));
	}

	public static Mono<ServerResponse> badRequest(ErrorType errorMsg) {
		return ServerResponse
				.badRequest()
				.body(BodyInserters.fromValue(getMessageDto(errorMsg)));
	}

	public static <T> Mono<ServerResponse> badRequest(T body) {
		return ServerResponse
				.badRequest()
				.bodyValue(body);
	}

	private static ErrorMsgDto getMessageDto(ErrorType messageError) {
		return ErrorMsgDto.builder()
				.text(messageError.getText())
				.build();
	}
}
