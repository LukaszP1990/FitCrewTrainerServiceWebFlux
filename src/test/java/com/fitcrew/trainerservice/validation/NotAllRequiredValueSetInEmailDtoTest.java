package com.fitcrew.trainerservice.validation;

import com.fitcrew.trainerservice.dto.EmailDto;
import com.fitcrew.trainerservice.dto.validation.NotAllRequiredValueSetInEmailDto;
import com.fitcrew.trainerservice.util.EmailUtil;
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
public class NotAllRequiredValueSetInEmailDtoTest {

    private static final EmailDto validEmailDto = EmailUtil.getEmailDto();
    private static final EmailDto notValidEmailDto = EmailDto.builder().build();

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private NotAllRequiredValueSetInEmailDto.NotAllRequiredValueSetInEmailDtoValidator emailValidator;

    @BeforeEach
    void setUp() {
        when(emailValidator.isValid(any(), any()))
                .thenCallRealMethod();
    }

    @Test
    void shouldSucceedWhenRequiredValuesHaveBeenSet() {
        assertTrue(emailValidator
                .isValid(validEmailDto, constraintValidatorContext));
    }

    @Test
    void shouldFailWhenWhenRequiredValuesHaveNotBeenSet() {
        assertFalse(emailValidator
                .isValid(notValidEmailDto, constraintValidatorContext));
    }
}
