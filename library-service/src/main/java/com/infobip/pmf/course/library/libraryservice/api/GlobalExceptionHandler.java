package com.infobip.pmf.course.library.libraryservice.api;

import com.infobip.pmf.course.library.libraryservice.exception.*;
import jakarta.servlet.http.HttpServletRequest;
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
        return new ErrorResponse("Library not found", "Use POST method to create a new library if needed.");
    }

    @ExceptionHandler(VersionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleVersionNotFoundException(VersionNotFoundException ex) {
        return new ErrorResponse("Version not found", "Ensure the version ID is correct and try again.");
    }

    @ExceptionHandler(LibraryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleLibraryAlreadyExistsException(LibraryAlreadyExistsException ex) {
        return new ErrorResponse("Library already exists", "Use PATCH method to update existing library or create new library with a different group and artifact.");
    }

    @ExceptionHandler(VersionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleVersionAlreadyExists(VersionAlreadyExistsException ex) {
        return new ErrorResponse("Version already exists", "Use PATCH method to update existing version or create new version with a different semantic version.");
    }

    @ExceptionHandler(InvalidFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidFilterException(InvalidFilterException ex) {
        return new ErrorResponse("Not a valid filter", "Check filter parameters and try again.");
    }

    @ExceptionHandler(VersionDeprecatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleVersionDeprecatedException(VersionDeprecatedException ex) {
        return new ErrorResponse("Deprecated version", "Use PATCH to update non deprecated versions");
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorResponse("Unauthorized", "Ensure valid API key in Authorization header.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidParameters(MethodArgumentNotValidException ex) {
        return new ErrorResponse(ex.getMessage(), "Ensure you have valid request body");
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
