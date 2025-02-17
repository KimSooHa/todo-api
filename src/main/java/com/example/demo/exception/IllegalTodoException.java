package com.example.demo.exception;

public class IllegalTodoException extends IllegalArgumentException {


    public IllegalTodoException(String message) {
        super(message);
    }

    public IllegalTodoException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTodoException(Throwable cause) {
        super(cause);
    }
}
