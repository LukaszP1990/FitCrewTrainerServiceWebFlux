package com.fitcrew.trainerservice.domains;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public
class RatingTrainerDocument implements Serializable {

	private static final long serialVersionUID = 1421658171867127534L;

	@Id
	private String id;

	@Field(value = "RATING")
	@NotNull
	private int rating;

	@Field(value = "FIRST_NAME")
	@NotNull
	@Length(max = 20)
	private String firstName;

	@Field(value = "LAST_NAME")
	@NotNull
	@Length(max = 20)
	private String lastName;

	@Field(value = "TRAINER_ID")
	@NotNull
	private Long trainerId;
}
