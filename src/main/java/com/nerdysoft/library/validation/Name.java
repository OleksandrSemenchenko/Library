package com.nerdysoft.library.validation;

import com.nerdysoft.library.validation.validators.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

  String message() default
      "The name should contain two capital words with name and surname and space between them";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
