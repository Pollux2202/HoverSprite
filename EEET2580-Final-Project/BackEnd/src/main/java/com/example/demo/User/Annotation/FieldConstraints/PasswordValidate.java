package com.example.demo.User.Annotation.FieldConstraints;

import com.example.demo.User.UserValidator.PasswordValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Constraint(validatedBy = PasswordValidation.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface PasswordValidate {
    String message() default "Password must be 8 to 15 characters, contain at least one capital letter. ONLY these special characters are allowed: %^@#!?.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}