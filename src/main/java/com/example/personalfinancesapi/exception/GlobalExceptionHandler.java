package com.example.personalfinancesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex) {
    HttpStatus status = (HttpStatus) ex.getStatus();
    String errorMessage = ex.getMessage();
    ErrorResponse errorResponse = new ErrorResponse(status.value(), errorMessage);
    return new ResponseEntity<>(errorResponse, status);
 }

 @ExceptionHandler(RuntimeException.class)
 public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
   ex.printStackTrace();
   
   HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
   String errorMessage = "An unexpected error occurred";
   ErrorResponse errorResponse = new ErrorResponse(status.value(), errorMessage);
   return new ResponseEntity<>(errorResponse, status);
 }
}
