package com.fitcrew.trainerservice.service.admin;

import com.fitcrew.FitCrewAppModel.domain.dto.TrainerDto;
import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.core.converter.TrainerConverterImpl;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import com.fitcrew.trainerservice.domains.TrainerDocument;
import com.fitcrew.trainerservice.service.cache.TrainerModelCache;
import com.fitcrew.trainerservice.service.cache.TrainerModelCacheService;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.fitcrew.trainerservice.util.TrainerUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminServiceTest {

    private final static List<TrainerDocument> trainerDocuments = TrainerUtil.getTrainerDocuments();
    private final static TrainerDocument trainerDocument = TrainerUtil.getTrainerDocument();
    private final static TrainerDto trainerDto = TrainerUtil.getTrainerDto();
    private final static TrainerModel trainerModel = TrainerUtil.getTrainerModel();

    private final TrainerRepository trainerRepository = Mockito.mock(TrainerRepository.class);
    private final TrainerModelCache trainerModelCache = Mockito.mock(TrainerModelCache.class);
    private final TrainerConverter trainerConverter = new TrainerConverterImpl();
    private final TrainerModelCacheService trainerModelCacheService = Mockito.mock(TrainerModelCacheService.class);
    private final AdminService adminService = new AdminService(trainerRepository, trainerConverter, trainerModelCache, trainerModelCacheService);

    @Test
    void shouldGetTrainers() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.fromIterable(trainerDocuments));

        //when
        var result = adminService.getTrainers();

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(clients -> 3 == clients.size())
                .verifyComplete();
    }

    @Test
    void shouldNotGetTrainersWhenTrainerTableInDatabaseIsEmpty() {
        //given
        when(trainerRepository.findAll())
                .thenReturn(Flux.empty());

        //when
        var result = adminService.getTrainers();

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(clients -> 0 == clients.size())
                .verifyComplete();
    }

    @Test
    void shouldGetTrainerFromCache() {
        //given
        when(trainerModelCacheService.getTrainerModel(anyString()))
                .thenReturn(Mono.just(trainerModel));

        //when
        var result = adminService.getTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotGetTrainerWhenItIsNotInCacheAndDatabase() {
        //given
        when(trainerModelCacheService.getTrainerModel(anyString()))
                .thenReturn(Mono.empty());

        //when
        var result = adminService.getTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldUpdateTrainer() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        when(trainerRepository.save(any()))
                .thenReturn(Mono.just(trainerDocument));
        doNothing()
                .when(trainerModelCache).put(anyString(), any(), anyLong());

        //when
        var result = adminService.updateTrainer(trainerDto, TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotUpdateTrainerWhenItCannotBeFoundInDatabaseByEmail() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());
        doNothing()
                .when(trainerModelCache).put(anyString(), any(), anyLong());

        //when
        var result = adminService.updateTrainer(trainerDto, TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldDeleteTrainer() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(trainerDocument));
        doNothing()
                .when(trainerModelCache).delete(anyString());

        //when
        var result = adminService.deleteTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(this::checkTrainerModelAssertions)
                .verifyComplete();
    }

    @Test
    void shouldNotDeleteClientWhenItCannotBeFoundInDatabaseByEmail() {
        //given
        when(trainerRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());
        doNothing()
                .when(trainerModelCache).delete(anyString());

        //when
        var result = adminService.deleteTrainer(TRAINER_EMAIL);

        //then
        StepVerifier.create(result)
                .expectSubscription()
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