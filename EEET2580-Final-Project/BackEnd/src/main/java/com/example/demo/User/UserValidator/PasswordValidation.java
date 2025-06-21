package com.example.demo.User.UserValidator;

import com.example.demo.User.Annotation.FieldConstraints.PasswordValidate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<PasswordValidate,String> {

    @Override
    public void initialize(PasswordValidate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if(password == null) {
            return false;
        }
        // Check for length
        boolean isLengthValid = password.length() >= 8 && password.length() <= 15;

        // Check for at least one capital letter
        boolean hasCapitalLetter = password.chars().anyMatch(Character::isUpperCase);

        // Check for allowed special characters
        boolean hasSpecialCharacter = password.chars().anyMatch(ch -> "%^@#!?.".indexOf(ch) >= 0);

        boolean isValid = isLengthValid && hasCapitalLetter && hasSpecialCharacter;
        System.out.println("Is password valid: " + isValid);
        // Combine all conditions
        return isValid;
    }
}
