package com.fitcrew.trainerservice.domains;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class TrainerDocument implements Serializable {

	private static final long serialVersionUID = 1421658171867127534L;

	@Id
	private String id;

	@Field(value = "FIRST_NAME")
	@NotNull
	@Length(max = 20)
	private String firstName;

	@Field(value = "LAST_NAME")
	@NotNull
	@Length(max = 20)
	private String lastName;

	@Field(value = "EMAIL")
	@NotNull
	@Indexed(unique = true)
	@Email
	private String email;

	@Field(value = "DATE_OF_BIRTH")
	@NotNull
	@Length(max = 20)
	private String dateOfBirth;

	@Field(value = "PHONE")
	@NotNull
	@Indexed(unique = true)
	@Pattern(regexp = "[0-9]{9}")
	private String phone;

	@Field(value = "PLACE_IN_THE_RANKING")
	@Indexed(unique = true)
	private String placeInTheRanking;

	@Field(value = "SOMETHING_ABOUT_YOURSELF")
	@NotNull
	@Length(max = 200)
	private String somethingAboutYourself;

	@Field(value = "TRAINER_ID")
	@NotNull
	@Indexed(unique = true)
	private String trainerId;

	@Field(value = "ENCRYPTED_PASSWORD")
	@NotNull
	@Indexed(unique = true)
	private String encryptedPassword;
}
