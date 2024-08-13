package com.nerdysoft.library.exceptionhandler;

import com.nerdysoft.library.exceptionhandler.exceptions.UnitNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

  private Map<String, Object> buildResponseBody(HttpStatus status, Object message) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(TIMESTAMP_FILED, LocalDateTime.now());
    body.put(ERROR_CODE_FIELD, status.value());
    body.put(DETAILS_FIELD, message);
    return body;
  }
}
