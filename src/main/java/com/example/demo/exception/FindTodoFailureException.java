package com.example.demo.exception;

public class FindTodoFailureException extends IllegalArgumentException {


    public FindTodoFailureException(String message) {
        super(message);
    }

    public FindTodoFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public FindTodoFailureException(Throwable cause) {
        super(cause);
    }
}
