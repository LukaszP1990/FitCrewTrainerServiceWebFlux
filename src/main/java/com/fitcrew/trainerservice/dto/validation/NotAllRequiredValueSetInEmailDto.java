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

import com.fitcrew.trainerservice.dto.EmailDto;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = NotAllRequiredValueSetInEmailDto.NotAllRequiredValueSetInEmailDtoValidator.class)
public @interface NotAllRequiredValueSetInEmailDto {
	String message() default "Not all required values has been set in email dto. This doesn't apply to the field filePathToAttachment";

	Class<?>[] groups() default {};

	Class[] payload() default {};

	class NotAllRequiredValueSetInEmailDtoValidator implements ConstraintValidator<NotAllRequiredValueSetInEmailDto, EmailDto> {

		@Override
		public boolean isValid(EmailDto emailDto,
							   ConstraintValidatorContext constraintValidatorContext) {
			return checkEmail(emailDto);
		}

		private boolean checkEmail(EmailDto emailDto) {
			List<? extends Comparable<? extends Comparable<?>>> listOfFields = Stream.of(
					emailDto.getSubject(),
					emailDto.getRecipient(),
					emailDto.getBodyOfMessage(),
					emailDto.getSender())
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
