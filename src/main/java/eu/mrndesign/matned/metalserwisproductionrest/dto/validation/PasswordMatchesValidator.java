package eu.mrndesign.matned.metalserwisproductionrest.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final PasswordValidationObjectInterface user = (PasswordValidationObjectInterface) obj;
        return user.getPassword().equals(user.getPasswordConfirm());
    }

}
