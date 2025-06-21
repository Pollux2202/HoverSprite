package com.example.demo.User.Employee.EmployeeValidator;


import com.example.demo.User.Employee.Annotations.FieldConstraints.EmployeeEmailConstraints;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEmailValidator implements ConstraintValidator<EmployeeEmailConstraints,String> {

    private static final String EMPLOYEE_EMAIL_DOMAIN = "@hoversprite.com";

    @Override
    public void initialize(EmployeeEmailConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email != null && email.endsWith(EMPLOYEE_EMAIL_DOMAIN)) {
            return true;
        }
        // If it doesn't, return false
        return false;
    }

}
