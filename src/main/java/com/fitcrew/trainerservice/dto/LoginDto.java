package com.fitcrew.trainerservice.dto;

import javax.validation.constraints.NotNull;

import com.fitcrew.FitCrewAppConstant.message.ValidationErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class LoginDto {

	@NotNull(message = ValidationErrorMessage.LOGIN_ERROR_MESSAGE)
	private String email;

	@NotNull(message = ValidationErrorMessage.PASSWORD_ERROR_MESSAGE)
	private String password;
}
