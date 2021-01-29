package com.fitcrew.trainerservice.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.trainerservice.domains.TrainerDocument;

import reactor.core.publisher.Mono;

@Repository
public interface TrainerRepository extends ReactiveMongoRepository<TrainerDocument, String> {
	Mono<TrainerDocument> findByEmail(String email);

    Mono<TrainerDocument> getTrainerByTrainerId(String trainerId);
}
