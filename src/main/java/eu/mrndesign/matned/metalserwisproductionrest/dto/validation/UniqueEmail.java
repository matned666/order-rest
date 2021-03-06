package eu.mrndesign.matned.metalserwisproductionrest.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "A user with the same login exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
