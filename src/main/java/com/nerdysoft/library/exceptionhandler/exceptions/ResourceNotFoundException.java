package com.nerdysoft.library.exceptionhandler.exceptions;

import com.nerdysoft.library.exceptionhandler.ExceptionMessages;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException() {
    super(ExceptionMessages.RESOURCE_NOT_FOUND);
  }
}
