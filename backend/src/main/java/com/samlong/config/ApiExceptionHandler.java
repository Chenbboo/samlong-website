package com.samlong.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
    Map<String, String> fieldErrors = new LinkedHashMap<String, String>();
    for (FieldError error : exception.getBindingResult().getFieldErrors()) {
      if (!fieldErrors.containsKey(error.getField())) fieldErrors.put(error.getField(), error.getDefaultMessage());
    }
    return response(HttpStatus.BAD_REQUEST, "Validation failed", "提交的数据不正确", fieldErrors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException exception) {
    Map<String, String> fieldErrors = new LinkedHashMap<String, String>();
    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      fieldErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return response(HttpStatus.BAD_REQUEST, "Validation failed", "提交的数据不正确", fieldErrors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException exception) {
    return response(HttpStatus.BAD_REQUEST, "Malformed request", "请求格式不正确", null);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException exception) {
    return response(HttpStatus.CONFLICT, "Data conflict", "数据与现有记录冲突或不符合约束", null);
  }

  private ResponseEntity<Map<String, Object>> response(HttpStatus status, String error, String message,
                                                         Map<String, String> fieldErrors) {
    Map<String, Object> body = new LinkedHashMap<String, Object>();
    body.put("status", status.value());
    body.put("error", error);
    body.put("message", message);
    if (fieldErrors != null && !fieldErrors.isEmpty()) body.put("fieldErrors", fieldErrors);
    return ResponseEntity.status(status).body(body);
  }
}
