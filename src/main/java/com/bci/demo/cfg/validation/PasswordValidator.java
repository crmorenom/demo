package com.bci.demo.cfg.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bci.demo.cfg.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordConfig passwordConfig;

    @Autowired
    public PasswordValidator(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        return password.matches(passwordConfig.getPasswordRegex());
    }
}
