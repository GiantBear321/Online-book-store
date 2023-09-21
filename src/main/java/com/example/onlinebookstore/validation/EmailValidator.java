package com.example.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {
    private static final String PATTERN_OF_EMAIL = "^(.+)@(\\S+)$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email.length() >= 6 && Pattern.compile(PATTERN_OF_EMAIL).matcher(email).matches();
    }
}
