package com.library.task.shared.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must contain at least one lowercase letter, one uppercase letter, and one number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}