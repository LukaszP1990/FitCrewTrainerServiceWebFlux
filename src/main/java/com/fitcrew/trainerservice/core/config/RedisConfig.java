package com.fitcrew.trainerservice.core.config;


import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.jwt.model.AuthenticationRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public ReactiveRedisTemplate<String, AuthenticationRequest> reactiveRedisTemplateForUser(ReactiveRedisConnectionFactory factory) {
		var valueSerializer = new Jackson2JsonRedisSerializer<>(AuthenticationRequest.class);
		return getReactiveRedisTemplate(valueSerializer, factory);
	}

	@Bean
	public ReactiveRedisTemplate<String, TrainerModel> reactiveRedisTemplateForTrainerModel(ReactiveRedisConnectionFactory factory) {
		var valueSerializer = new Jackson2JsonRedisSerializer<>(TrainerModel.class);
		return getReactiveRedisTemplate(valueSerializer, factory);
	}

	private <T> ReactiveRedisTemplate<String, T> getReactiveRedisTemplate(Jackson2JsonRedisSerializer<T> valueSerializer,
																		  ReactiveRedisConnectionFactory factory) {
		var keySerializer = new StringRedisSerializer();
		RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
				RedisSerializationContext.newSerializationContext(keySerializer);
		RedisSerializationContext<String, T> context =
				builder.value(valueSerializer).build();

		return new ReactiveRedisTemplate<>(factory, context);
	}
}
