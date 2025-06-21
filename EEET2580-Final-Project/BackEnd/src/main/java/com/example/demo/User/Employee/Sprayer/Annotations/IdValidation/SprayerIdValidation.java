package com.example.demo.User.Employee.Sprayer.Annotations.IdValidation;

import com.example.demo.User.Employee.Sprayer.SprayerValidator.SprayerIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SprayerIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SprayerIdValidation {
    String message() default "Invalid ID format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
