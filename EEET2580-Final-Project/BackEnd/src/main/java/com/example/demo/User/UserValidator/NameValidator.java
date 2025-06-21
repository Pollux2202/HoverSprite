package com.example.demo.User.UserValidator;

import com.example.demo.User.Annotation.FieldConstraints.NameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {

    // Allows two non-adjacent capitalized letters in a word and supports Vietnamese characters
    private static final String NAME_PATTERN = "^(?!.*[A-Z]{3,})([A-Z]?[\\p{L}&&[^\\p{InCombiningDiacriticalMarks}]]+([\\p{M}]+)?|[A-Z][\\p{L}&&[^\\p{InCombiningDiacriticalMarks}]]*[A-Z][\\p{L}&&[^\\p{InCombiningDiacriticalMarks}]]*([\\p{M}]+)?)+(\\s+[A-Z]?[\\p{L}&&[^\\p{InCombiningDiacriticalMarks}]]+([\\p{M}]+)?|[A-Z][\\p{L}&&[^\\p{InCombiningDiacriticalMarks}]]*[A-Z][\\p{L}&&[^\\p{InCombiningDiacriticalMarks}]]*([\\p{M}]+)?)*$";

    @Override
    public void initialize(NameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isEmpty()) {
            return false; // Return false for null or empty names (you can modify this as per your needs)
        }
        return name.matches(NAME_PATTERN);
    }
}
