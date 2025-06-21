package com.example.demo.User.Customer.Farmer.FarmerValidator;


import com.example.demo.User.Customer.Farmer.Annotations.IdValidation.FarmerIdValidation;
import com.example.demo.User.Customer.Farmer.FarmerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FarmerIdValidator implements ConstraintValidator<FarmerIdValidation, String> {

    @Autowired
    private FarmerRepository farmerRepository;

    @Override
    public void initialize(FarmerIdValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String fId, ConstraintValidatorContext constraintValidatorContext) {
        if (fId == null || !fId.startsWith("f")) {
            return false;
        }

        // Additional logic to check uniqueness or other conditions can be added here
        return true;
    }

    public String generateId() {
        long count = farmerRepository.count();

        return "f" + (count + 1);
    }
}
