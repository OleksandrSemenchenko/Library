package com.nerdysoft.library.validation.validators;

import com.nerdysoft.library.validation.AuthorName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AuthorNameValidator implements ConstraintValidator<AuthorName, String> {

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
    String[] fistnameAndSurname = name.split(" ");
    return Arrays.stream(fistnameAndSurname)
        .allMatch(word -> Character.isUpperCase(word.charAt(0)));
  }
}
