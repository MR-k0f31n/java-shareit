package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * @author MR.k0F31N
 */

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNullPointerException(final NullPointerException exception) {
        log.warn("Error! NullPointer, server status: '{}' text message: '{}'",
                HttpStatus.BAD_REQUEST, exception.getMessage());
        return Map.of("Null detected, check your actions", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnsupportedStatusException(final IllegalArgumentException exception) {
        log.warn("Error! Unsupported Status, server status: '{}' text message: '{}'",
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return Map.of("error", exception.getMessage());
    }
}
