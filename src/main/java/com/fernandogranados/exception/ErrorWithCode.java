package com.fernandogranados.exception;

public final class ErrorWithCode extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final String code;

  public ErrorWithCode(String code, String message) {
    this(code, message, null);
  }

  public ErrorWithCode(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
