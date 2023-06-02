/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidationException(final ValidatorException exception) {
        log.warn("Error! Validation fault, server status: '{}' text message: '{}'",
                HttpStatus.BAD_REQUEST, exception.getMessage());
        return Map.of("Validation fault, check your actions", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidException(final MethodArgumentNotValidException exception) {
        log.warn("Error! Validation fault, server status: '{}' text message: '{}'",
                HttpStatus.BAD_REQUEST, exception.getMessage());
        return Map.of("Validation object fault, check your actions", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handlerEmailConflict(final EmailConflictException exception) {
        log.warn("Error! Validation fault, server status: '{}' text message: '{}'",
                HttpStatus.CONFLICT, exception.getMessage());
        return Map.of("EMAIL ERROR. ", "This email exist");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnsupportedStatusException(final UnsupportedStatus exception) {
        log.warn("Error! Unsupported Status, server status: '{}' text message: '{}'",
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> IncorrectDataExeption(final IncorrectDataExeption exception) {
        log.warn("Error! server status: '{}' text message: '{}'",
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return Map.of("error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> ConstraintViolationException(final ConstraintViolationException exception) {
        log.warn("Error! server status: '{}' text message: '{}'",
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return Map.of("error", exception.getMessage());
    }
}
