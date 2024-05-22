package com.infobip.pmf.course.library.libraryservice.api;

import com.infobip.pmf.course.library.libraryservice.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibraryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleLibraryNotFoundException(LibraryNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), ex.getAction());
    }

    @ExceptionHandler(VersionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleVersionNotFoundException(VersionNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), ex.getAction());
    }

    @ExceptionHandler(LibraryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleLibraryAlreadyExistsException(LibraryAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage(), ex.getAction());
    }

    @ExceptionHandler(VersionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleVersionAlreadyExists(VersionAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage(), ex.getAction());
    }

    @ExceptionHandler(InvalidFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidFilterException(InvalidFilterException ex) {
        return new ErrorResponse(ex.getMessage(), ex.getAction());
    }

    @ExceptionHandler(VersionDeprecatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleVersionDeprecatedException(VersionDeprecatedException ex) {
        return new ErrorResponse(ex.getMessage(), ex.getAction());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidParameters(MethodArgumentNotValidException ex) {
        return new ErrorResponse(ex.getMessage(), "Ensure you have valid request body");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorResponse(ex.getMessage(), "Ensure you have valid request parameters");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
        return new ErrorResponse(ex.getMessage(), "Please contact support.");
    }

    public record ErrorResponse(String message, String action) {
    }
}
