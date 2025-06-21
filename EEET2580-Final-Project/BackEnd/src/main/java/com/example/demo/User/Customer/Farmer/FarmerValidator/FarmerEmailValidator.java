package com.example.demo.User.Customer.Farmer.FarmerValidator;


import com.example.demo.User.Customer.Farmer.Annotations.FieldConstraints.FarmerEmailConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class FarmerEmailValidator implements ConstraintValidator<FarmerEmailConstraint, String> {

    private static final String EMAIL_PATTERN = "^[\\w._%+-]+@[\\w.-]+\\.(com|vn)$";

    @Override
    public void initialize(FarmerEmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false; // Return false for null values (you can modify this as per your needs)
        }
        return email.matches(EMAIL_PATTERN);
    }
}
