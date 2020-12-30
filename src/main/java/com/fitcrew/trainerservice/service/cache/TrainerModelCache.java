package com.fitcrew.trainerservice.service.cache;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class TrainerModelCache {

    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, TrainerModel> trainerOps;

    public TrainerModelCache(ReactiveRedisConnectionFactory factory,
                             ReactiveRedisOperations<String, TrainerModel> trainerOps) {
        this.factory = factory;
        this.trainerOps = trainerOps;
    }

    public void put(String key,
                    TrainerModel trainerModel,
                    long hour) {
        trainerOps.opsForValue().set(key, trainerModel, Duration.ofHours(hour))
                .subscribe(result -> log.info("Add trainerModel: {} to cache", trainerModel));
    }

    public Mono<TrainerModel> get(String key) {
        log.info("Get trainerModel by key: {} from cache", key);
        return trainerOps.opsForValue().get(key);
    }

    public void delete(String key) {
        trainerOps.opsForValue().delete(key)
                .subscribe(result -> log.info("Delete trainerModel by key: {} from cache", key));
    }
}
