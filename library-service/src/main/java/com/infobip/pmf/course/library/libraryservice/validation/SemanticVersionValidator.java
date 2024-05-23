package com.infobip.pmf.course.library.libraryservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SemanticVersionValidator implements ConstraintValidator<ValidSemanticVersion, String> {

    private static final String SEMVER_PATTERN = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$";

    @Override
    public void initialize(ValidSemanticVersion constraintAnnotation) {
    }

    @Override
    public boolean isValid(String semanticVersion, ConstraintValidatorContext context) {
        return semanticVersion != null && semanticVersion.matches(SEMVER_PATTERN);
    }
}

