package com.example.demo.User.UserValidator;

import com.example.demo.User.Annotation.FieldConstraints.PhoneNumberConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    private static final String PHONE_NUMBER_PATTERN = "^(0|\\+84)\\s?\\d{2,3}(\\s?\\d{3,4}){2}$";

    @Override
    public void initialize(PhoneNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false; // Return false for null values (you can modify this as per your needs)
        }
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}

