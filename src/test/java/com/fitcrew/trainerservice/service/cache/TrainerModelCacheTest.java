package com.fitcrew.trainerservice.service.cache;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Objects;

import static com.fitcrew.trainerservice.util.TrainerUtil.TRAINER_EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerModelCacheTest {

    private static final TrainerModel trainerModel = TrainerUtil.getTrainerModel();

    @Mock
    private ReactiveRedisConnectionFactory factory;
    @Mock
    private ReactiveRedisOperations<String, TrainerModel> trainerOps;
    @Mock
    private ReactiveValueOperations<String, TrainerModel> reactiveValueOperations;

    @InjectMocks
    private TrainerModelCache trainerModelCache;

    @Test
    void put() {
        //given
        setField(trainerModelCache, "factory", factory);
        setField(trainerModelCache, "trainerOps", trainerOps);
        when(trainerOps.opsForValue())
                .thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.set(anyString(), any(), any()))
                .thenReturn(Mono.just(true));

        //when
        trainerModelCache.put(TRAINER_EMAIL, trainerModel, 1);

        //then
        verify(reactiveValueOperations, times(1)).set(TRAINER_EMAIL, trainerModel, Duration.ofHours(1));
    }

    @Test
    void get() {
        //given
        setField(trainerModelCache, "factory", factory);
        setField(trainerModelCache, "trainerOps", trainerOps);
        when(trainerOps.opsForValue())
                .thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.get(anyString()))
                .thenReturn(Mono.just(trainerModel));

        //when
        var result = trainerModelCache.get(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

    @Test
    void delete() {
        //given
        setField(trainerModelCache, "factory", factory);
        setField(trainerModelCache, "trainerOps", trainerOps);
        when(trainerOps.opsForValue())
                .thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.delete(anyString()))
                .thenReturn(Mono.just(true));

        //when
        trainerModelCache.delete(TRAINER_EMAIL);

        //then
        verify(reactiveValueOperations, times(1)).delete(TRAINER_EMAIL);
    }
}