package com.example.demo.User.Annotation.FieldConstraints;

import com.example.demo.User.UserValidator.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameConstraint {
    String message() default "Invalid name format.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
