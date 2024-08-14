package com.nerdysoft.library.validation;

import com.nerdysoft.library.validation.validators.BookTitleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookTitleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookTitle {

  String message() default
      "The book title should start with a capital letter and have 3 symbols minimum length";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
