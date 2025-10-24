package com.bci.demo.cfg.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PasswordValidatorTest {

    @InjectMocks
    private PasswordValidator passwordValidator;

    @Mock
    private PasswordConfig passwordConfig;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    public void setUp() {
        when(passwordConfig.getPasswordRegex()).thenReturn("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        passwordValidator.initialize(null);
    }

    @Test
    public void testIsValid_validPassword() {
        assertTrue(passwordValidator.isValid("ValidPass@1", constraintValidatorContext));
    }

    @Test
    public void testIsValid_nullPassword() {
        assertFalse(passwordValidator.isValid(null, constraintValidatorContext));
    }

    @Test
    public void testIsValid_noNumber() {
        assertFalse(passwordValidator.isValid("ValidPass@", constraintValidatorContext));
    }

    @Test
    public void testIsValid_noLowercase() {
        assertFalse(passwordValidator.isValid("VALIDPASS@1", constraintValidatorContext));
    }

    @Test
    public void testIsValid_noUppercase() {
        assertFalse(passwordValidator.isValid("validpass@1", constraintValidatorContext));
    }

    @Test
    public void testIsValid_noSpecialChar() {
        assertFalse(passwordValidator.isValid("ValidPass1", constraintValidatorContext));
    }

    @Test
    public void testIsValid_tooShort() {
        assertFalse(passwordValidator.isValid("Val@1", constraintValidatorContext));
    }

    @Test
    public void testIsValid_withWhitespace() {
        assertFalse(passwordValidator.isValid("Valid Pass@1", constraintValidatorContext));
    }
}
