package com.nerdysoft.library.validation.validators;

import com.nerdysoft.library.validation.Name;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class NameValidator implements ConstraintValidator<Name, String> {

  @Override
  public boolean isValid(String name, ConstraintValidatorContext context) {
    return hasNameOneSpace(name) && isFirstLetterOfEachWordUpperCase(name);
  }

  private boolean hasNameOneSpace(String name) {
    char[] charArray = name.toCharArray();
    int whiteSpaceQuantity = 0;

    for (char literal : charArray) {
      if (Character.isWhitespace(literal)) {
        whiteSpaceQuantity++;
      }
    }
    return whiteSpaceQuantity == 1;
  }

  private boolean isFirstLetterOfEachWordUpperCase(String name) {
    String[] firstnameAndSurname = name.split(" ");
    return Arrays.stream(firstnameAndSurname)
        .allMatch(word -> Character.isUpperCase(word.charAt(0)));
  }
}
