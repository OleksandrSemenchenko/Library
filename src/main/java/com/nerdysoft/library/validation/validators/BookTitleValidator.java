package com.nerdysoft.library.validation.validators;

import static java.util.Objects.nonNull;

import com.nerdysoft.library.validation.BookTitle;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookTitleValidator implements ConstraintValidator<BookTitle, String> {

  private static final int MIN_LENGTH = 3;

  @Override
  public boolean isValid(String bookTitleFormat, ConstraintValidatorContext context) {
    return nonNull(bookTitleFormat) && isBookTitleFormatValid(bookTitleFormat);
  }

  private boolean isBookTitleFormatValid(String title) {
    char[] titleCharArray = title.toCharArray();
    return hasMinLength(titleCharArray) && isFirstLetterCapital(titleCharArray);
  }

  private boolean hasMinLength(char[] charArray) {
    return charArray.length >= MIN_LENGTH;
  }

  private boolean isFirstLetterCapital(char[] charArray) {
    return Character.isUpperCase(charArray[0]);
  }
}
