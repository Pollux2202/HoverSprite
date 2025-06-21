package com.example.demo.User.Employee.Sprayer.SprayerValidator;



import com.example.demo.User.Employee.Sprayer.Annotations.IdValidation.SprayerIdValidation;
import com.example.demo.User.Employee.Sprayer.SprayerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SprayerIdValidator implements ConstraintValidator<SprayerIdValidation, String> {

    @Autowired
    private SprayerRepository sprayerRepository;

    @Override
    public void initialize(SprayerIdValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String sId, ConstraintValidatorContext constraintValidatorContext) {
        if(sId == null || !sId.startsWith("s")){
            return false;
        }
        return true;
    }

    public String generateId(){
        long count = sprayerRepository.count();

        return "s" + (count + 1);
    }
}
