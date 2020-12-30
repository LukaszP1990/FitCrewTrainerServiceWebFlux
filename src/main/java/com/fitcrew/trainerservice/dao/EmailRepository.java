package com.fitcrew.trainerservice.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.fitcrew.trainerservice.domains.EmailDocument;

@Repository
public interface EmailRepository extends ReactiveMongoRepository<EmailDocument, String> {
}
