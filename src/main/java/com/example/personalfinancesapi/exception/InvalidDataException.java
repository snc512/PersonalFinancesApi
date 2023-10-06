package com.example.personalfinancesapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends RuntimeException{
  private final HttpStatus status;
  private final String message;

  public InvalidDataException(HttpStatus status, String message) {
      this.status = status;
      this.message = message;
  }

  public HttpStatus getStatus() {
      return status;
  }

  @Override
  public String getMessage() {
      return message;
  }
}
