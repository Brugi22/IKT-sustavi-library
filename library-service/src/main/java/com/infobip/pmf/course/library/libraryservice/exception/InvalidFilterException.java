package com.infobip.pmf.course.library.libraryservice.exception;

public class InvalidFilterException extends RuntimeException {
    private final String message;
    private final String action;

    public InvalidFilterException(String message, String action) {
        this.message = message;
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }
}
