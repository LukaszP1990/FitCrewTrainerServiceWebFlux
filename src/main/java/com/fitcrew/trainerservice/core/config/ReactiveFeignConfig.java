package com.fitcrew.trainerservice.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactivefeign.webclient.WebReactiveOptions;

@Configuration
public class ReactiveFeignConfig {

	@Bean
	public WebReactiveOptions webReactiveOptions() {
		return new WebReactiveOptions.Builder()
				.setWriteTimeoutMillis(Long.MAX_VALUE)
				.setReadTimeoutMillis(Long.MAX_VALUE)
				.build();
	}
}
