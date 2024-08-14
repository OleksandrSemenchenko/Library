package com.nerdysoft.library.exceptionhandler;

import com.nerdysoft.library.exceptionhandler.exceptions.ActionForbiddenException;
import com.nerdysoft.library.exceptionhandler.exceptions.UnitNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The exception handler for the whole service. Exceptions arising when calling a controller are
 * handled here.
 *
 * @author Oleksandr Semenchenko
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandler {

  private static final String DETAILS_FIELD = "details";
  private static final String ERROR_CODE_FIELD = "errorCode";
  private static final String TIMESTAMP_FILED = "timestamp";

  @ExceptionHandler(UnitNotFoundException.class)
  protected ResponseEntity<Object> handleUnitNotFoundException(UnitNotFoundException e) {
    Map<String, Object> responseBody = buildResponseBody(HttpStatus.NOT_FOUND, e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
  }

  @ExceptionHandler(ActionForbiddenException.class)
  protected ResponseEntity<Object> handleActionForbiddenException(ActionForbiddenException e) {
    Map<String, Object> responseBody = buildResponseBody(HttpStatus.FORBIDDEN, e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    Map<String, String> details = getMethodArgumentValidationDetails(e);
    Map<String, Object> responseBody = buildResponseBody(HttpStatus.BAD_REQUEST, details);
    return ResponseEntity.badRequest().body(responseBody);
  }

  private Map<String, String> getMethodArgumentValidationDetails(
      MethodArgumentNotValidException e) {
    return e.getBindingResult().getFieldErrors().stream()
        .collect(
            Collectors.toMap(FieldError::getField, MessageSourceResolvable::getDefaultMessage));
  }

  private Map<String, Object> buildResponseBody(HttpStatus status, Object message) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(TIMESTAMP_FILED, LocalDateTime.now());
    body.put(ERROR_CODE_FIELD, status.value());
    body.put(DETAILS_FIELD, message);
    return body;
  }
}
