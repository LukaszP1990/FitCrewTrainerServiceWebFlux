package com.fitcrew.trainerservice.validation;

import com.fitcrew.trainerservice.dto.TrainerDto;
import com.fitcrew.trainerservice.dto.validation.NotAllRequiredValueSetInTrainerDto;
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
public class NotAllRequiredValueSetInTrainerDtoTest {

    private static final TrainerDto validTrainerDto = TrainerUtil.getTrainerDto();
    private static final TrainerDto notValidTrainerDto = TrainerDto.builder().build();

    @Mock
    private NotAllRequiredValueSetInTrainerDto.NotAllRequiredValueSetInTrainerDtoValidator trainerValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    void setUp() {
        when(trainerValidator.isValid(any(), any()))
                .thenCallRealMethod();
    }

    @Test
    void shouldSucceedWhenRequiredValuesHaveBeenSet() {
        assertTrue(trainerValidator
                .isValid(validTrainerDto, constraintValidatorContext));
    }

    @Test
    void shouldFailWhenWhenRequiredValuesHaveNotBeenSet() {
        assertFalse(trainerValidator
                .isValid(notValidTrainerDto, constraintValidatorContext));
    }
}
