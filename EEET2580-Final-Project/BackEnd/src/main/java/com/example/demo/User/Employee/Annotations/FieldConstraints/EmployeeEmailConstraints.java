package com.example.demo.User.Employee.Annotations.FieldConstraints;


import com.example.demo.User.Employee.EmployeeValidator.EmployeeEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmployeeEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmployeeEmailConstraints {
    String message() default "Invalid email format. For HoverSprite employee, the domain should be @hoversprite.com";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
