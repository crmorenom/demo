package com.bci.demo.cfg.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

    @Value("${user.password.regex}")
    private String passwordRegex;

    public String getPasswordRegex() {
        return passwordRegex;
    }
}
