package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.warn("Error! Not Found, server status: '{}' text message: '{}'", HttpStatus.NOT_FOUND, exception.getMessage());
        return Map.of("Not found object", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullPointerException(final NullPointerException exception) {
        log.warn("Error! NullPointer, server status: '{}' text message: '{}'",
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return Map.of("Null detected, check your actions", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable exception) {
        log.warn("Error! Server status: '{}' text message: '{}'", HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return Map.of("Server Error", exception.getMessage());
    }
}
