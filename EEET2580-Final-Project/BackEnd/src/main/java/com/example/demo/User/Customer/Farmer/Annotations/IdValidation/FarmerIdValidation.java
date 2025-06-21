package com.example.demo.User.Customer.Farmer.Annotations.IdValidation;

import com.example.demo.User.Customer.Farmer.FarmerValidator.FarmerIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = FarmerIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FarmerIdValidation {
    String message() default "Invalid ID format";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
