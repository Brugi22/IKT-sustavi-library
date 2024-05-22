package com.infobip.pmf.course.library.libraryservice.exception;

public class VersionDeprecatedException extends RuntimeException{
    private final String message;
    private final String action;

    public VersionDeprecatedException(String message, String action) {
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
