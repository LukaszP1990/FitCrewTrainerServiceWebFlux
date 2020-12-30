package com.fitcrew.trainerservice.service.cache;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.core.converter.TrainerConverterImpl;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerModelCacheServiceTest {

    private static final TrainerModel trainerModel = TrainerUtil.getTrainerModel();
    private static final TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();

    private final TrainerRepository trainerRepository = Mockito.mock(TrainerRepository.class);
    private final TrainerModelCache trainerModelCache = Mockito.mock(TrainerModelCache.class);
    private final TrainerConverter trainerConverter = new TrainerConverterImpl();
    private final TrainerModelCacheService trainerModelCacheService = new TrainerModelCacheService(trainerRepository, trainerConverter, trainerModelCache);

    @Test
    void shouldGetTrainerModelFromCache() {
        when(trainerModelCache.get(anyString()))
                .thenReturn(Mono.just(trainerModel));

        StepVerifier.create(trainerModelCacheService.getTrainerModel(TRAINER_EMAIL))
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldGetTrainerModelFromDatabase() {
        when(trainerModelCache.get(anyString()))
                .thenReturn(Mono.empty());

        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        doNothing()
                .when(trainerModelCache).put(anyString(), any(), anyLong());

        StepVerifier.create(trainerModelCacheService.getTrainerModel(TRAINER_EMAIL))
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    private boolean checkTrainerModelAssertions(TrainerModel trainerModel) {
        return String.valueOf(1).equals(trainerModel.getTrainerId()) &&
                TRAINER_ENCRYPTED_PASSWORD.equals(trainerModel.getEncryptedPassword()) &&
                TRAINER_FIRST_NAME.equals(trainerModel.getFirstName()) &&
                String.valueOf(1).equals(trainerModel.getPlaceInTheRanking()) &&
                TRAINER_LAST_NAME.equals(trainerModel.getLastName()) &&
                TRAINER_DATE_OF_BIRTH.equals(trainerModel.getDateOfBirth()) &&
                String.valueOf(1).concat(TRAINER_EMAIL).equals(trainerModel.getEmail()) &&
                TRAINER_DESCRIPTION.equals(trainerModel.getSomethingAboutYourself()) &&
                TRAINER_PHONE_NUMBER.equals(trainerModel.getPhone());
    }
}