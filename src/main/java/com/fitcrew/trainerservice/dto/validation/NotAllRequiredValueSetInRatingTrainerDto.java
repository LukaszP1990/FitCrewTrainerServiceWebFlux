package com.fitcrew.trainerservice.dto.validation;


import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.fitcrew.trainerservice.dto.RatingTrainerDto;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = NotAllRequiredValueSetInRatingTrainerDto.NotAllRequiredValueSetInRatingTrainerDtoValidator.class)
public @interface NotAllRequiredValueSetInRatingTrainerDto {
	String message() default "Not all required values has been set in rating trainer dto.";

	Class<?>[] groups() default {};

	Class[] payload() default {};

	class NotAllRequiredValueSetInRatingTrainerDtoValidator implements ConstraintValidator<NotAllRequiredValueSetInRatingTrainerDto, RatingTrainerDto> {

		@Override
		public boolean isValid(RatingTrainerDto ratingTrainerDto,
							   ConstraintValidatorContext constraintValidatorContext) {
			return checkRating(ratingTrainerDto);
		}

		private boolean checkRating(RatingTrainerDto ratingTrainerDto) {
			List<? extends Comparable<? extends Comparable<?>>> listOfFields = Stream.of(
					ratingTrainerDto.getFirstName(),
					ratingTrainerDto.getLastName(),
					ratingTrainerDto.getRating())
					.collect(Collectors.toList());
			return validateFields(listOfFields);
		}

		private boolean validateFields(List<? extends Comparable<? extends Comparable<?>>> listOfFields) {
			return Optional.ofNullable(listOfFields)
					.map(field -> isField(listOfFields))
					.orElse(false);
		}

		private boolean isField(List<? extends Comparable<? extends Comparable<?>>> listOfFields) {
			return IntStream.rangeClosed(0, listOfFields.size() - 1)
					.allMatch(value -> Objects.nonNull(listOfFields.get(value)));
		}
	}
}
