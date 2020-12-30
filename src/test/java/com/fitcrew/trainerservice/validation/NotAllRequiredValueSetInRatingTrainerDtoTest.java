package com.fitcrew.trainerservice.validation;

import com.fitcrew.trainerservice.dto.RatingTrainerDto;
import com.fitcrew.trainerservice.dto.validation.NotAllRequiredValueSetInRatingTrainerDto;
import com.fitcrew.trainerservice.util.TrainerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotAllRequiredValueSetInRatingTrainerDtoTest {

    private static final RatingTrainerDto validRatingTrainerDto = TrainerUtil.getRatingTrainerDto();
    private static final RatingTrainerDto notValidRatingTrainerDto = RatingTrainerDto.builder().build();

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private NotAllRequiredValueSetInRatingTrainerDto.NotAllRequiredValueSetInRatingTrainerDtoValidator ratingTrainerValidator;

    @BeforeEach
    void setUp() {
        when(ratingTrainerValidator.isValid(any(), any()))
                .thenCallRealMethod();
    }

    @Test
    void shouldSucceedWhenRequiredValuesHaveBeenSet() {
        assertTrue(ratingTrainerValidator
                .isValid(validRatingTrainerDto, constraintValidatorContext));
    }

    @Test
    void shouldFailWhenWhenRequiredValuesHaveNotBeenSet() {
        assertFalse(ratingTrainerValidator
                .isValid(notValidRatingTrainerDto, constraintValidatorContext));
    }
}
