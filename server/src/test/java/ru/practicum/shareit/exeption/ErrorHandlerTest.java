package ru.practicum.shareit.exeption;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void testHandleUnsupportedStateException() {
        UnsupportedStatus exception = new UnsupportedStatus("error");
        Map<String, String> result = errorHandler.handleUnsupportedStatusException(exception);
        assertEquals("error", result.get("error"));
    }

    @Test
    void testHandleNullPointerException() {
        NullPointerException exception = new NullPointerException("error");
        Map<String, String> result = errorHandler.handleNullPointerException(exception);
        assertEquals("error", result.get("Null detected, check your actions"));
    }

    @Test
    void testHandlerValidationException() {
        ValidatorException exception = new ValidatorException("error");
        Map<String, String> result = errorHandler.handlerValidationException(exception);
        assertEquals("error", result.get("Validation fault, check your actions"));
    }

    @Test
    void testHandlerEmailConflict() {
        EmailConflictException exception = new EmailConflictException("This email exist");
        Map<String, String> result = errorHandler.handlerEmailConflict(exception);
        assertEquals("This email exist", result.get("EMAIL ERROR"));
    }

    @Test
    void testIncorrectDataExeption() {
        IncorrectDataExeption exception = new IncorrectDataExeption("This email exist");
        Map<String, String> result = errorHandler.incorrectDataExeption(exception);
        assertEquals("This email exist", result.get("error"));
    }
}
