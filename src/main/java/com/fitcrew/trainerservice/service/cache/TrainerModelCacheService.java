package com.fitcrew.trainerservice.service.cache;

import com.fitcrew.FitCrewAppModel.domain.model.TrainerModel;
import com.fitcrew.trainerservice.core.converter.TrainerConverter;
import com.fitcrew.trainerservice.dao.TrainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
public class TrainerModelCacheService {

	private final TrainerRepository trainerRepository;
	private final TrainerConverter trainerConverter;
	private final TrainerModelCache trainerModelCache;

	public TrainerModelCacheService(final TrainerRepository trainerRepository,
									final TrainerConverter trainerConverter,
									final TrainerModelCache trainerModelCache) {
		this.trainerRepository = trainerRepository;
		this.trainerConverter = trainerConverter;
		this.trainerModelCache = trainerModelCache;
	}

	public Mono<TrainerModel> getTrainerModel(String trainerEmail) {
		return trainerModelCache.get(trainerEmail)
				.switchIfEmpty(Mono.defer(() -> getTrainerModelFromDb(trainerEmail)));
	}

	private Mono<TrainerModel> getTrainerModelFromDb(String trainerEmail) {
		return trainerRepository.findByEmail(trainerEmail)
				.filter(Objects::nonNull)
				.map(trainerConverter::trainerDocumentToTrainerModel)
				.doOnSuccess(trainerModel -> trainerModelCache.put(trainerEmail, trainerModel, 5));
	}
}
