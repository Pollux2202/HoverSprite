package com.example.demo.User.Employee.Receptionist.ReceptionistValidator;



import com.example.demo.User.Employee.Receptionist.Annotations.IdValidation.ReceptionistIdValidation;
import com.example.demo.User.Employee.Receptionist.ReceptionistRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistIdValidator implements ConstraintValidator<ReceptionistIdValidation, String> {

    @Autowired
    ReceptionistRepository receptionistRepository;
    @Override
    public void initialize(ReceptionistIdValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String rId, ConstraintValidatorContext constraintValidatorContext) {
        if (rId == null || !rId.startsWith("r")) {
            return false;
        }
        // Additional logic to check uniqueness or other conditions can be added here
        return true;
    }

    public String generateId() {
        long count = receptionistRepository.count();

        return "r" + (count + 1);
    }
}

