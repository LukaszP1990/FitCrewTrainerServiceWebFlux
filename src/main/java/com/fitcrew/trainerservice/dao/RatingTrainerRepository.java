package com.fitcrew.trainerservice.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.trainerservice.domains.RatingTrainerDocument;

import reactor.core.publisher.Flux;

@Repository
public interface RatingTrainerRepository extends ReactiveMongoRepository<RatingTrainerDocument, String> {
	Flux<RatingTrainerDocument> findByTrainerId(Long id);
}
