package com.fitcrew.trainerservice.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainingServiceConstant {
	private static final String SERVICE_NAME = "fitcrewtrainingservice";

	public static final String TRAINING_RESOURCE = SERVICE_NAME + "/api/training";
}
