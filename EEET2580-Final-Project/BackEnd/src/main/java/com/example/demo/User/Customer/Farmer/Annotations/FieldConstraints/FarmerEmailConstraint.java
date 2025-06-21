package com.example.demo.User.Customer.Farmer.Annotations.FieldConstraints;


import com.example.demo.User.Customer.Farmer.FarmerValidator.FarmerEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FarmerEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FarmerEmailConstraint {
    String message() default "Invalid email format.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
