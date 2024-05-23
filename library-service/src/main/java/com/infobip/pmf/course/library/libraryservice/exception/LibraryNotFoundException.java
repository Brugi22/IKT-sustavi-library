package com.infobip.pmf.course.library.libraryservice.exception;

public class LibraryNotFoundException extends RuntimeException{
    private final String message;
    private final String action;

    public LibraryNotFoundException(String message, String action) {
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
