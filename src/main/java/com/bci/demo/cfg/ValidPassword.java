package com.bci.demo.cfg;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.bci.demo.cfg.validation.PasswordValidator;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "La contraseña no cumple con el formato requerido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
