package com.fitcrew.trainerservice.dto.validation;

import com.fitcrew.trainerservice.dto.TrainerDto;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = NotAllRequiredValueSetInTrainerDto.NotAllRequiredValueSetInTrainerDtoValidator.class)
public @interface NotAllRequiredValueSetInTrainerDto {
	String message() default "Not all required values has been set in trainer dto. This doesn't apply to the field trainerId and encryptedPassword;";

	Class<?>[] groups() default {};

	Class[] payload() default {};

	class NotAllRequiredValueSetInTrainerDtoValidator implements ConstraintValidator<NotAllRequiredValueSetInTrainerDto, TrainerDto> {

		@Override
		public boolean isValid(TrainerDto trainerDto,
							   ConstraintValidatorContext constraintValidatorContext) {
			return checkRating(trainerDto);
		}

		private boolean checkRating(TrainerDto trainerDto) {
			List<? extends Comparable<? extends Comparable<?>>> listOfFields = Stream.of(
					trainerDto.getFirstName(),
					trainerDto.getLastName(),
					trainerDto.getEmail(),
					trainerDto.getPhone(),
					trainerDto.getDateOfBirth(),
					trainerDto.getPlaceInTheRanking(),
					trainerDto.getSomethingAboutYourself(),
					trainerDto.getPassword())
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
