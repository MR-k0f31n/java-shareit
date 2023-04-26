package ru.practicum.shareit.exception;

public class EmailConflictExeption extends RuntimeException {
    public EmailConflictExeption(String message) {
        super(message);
    }
}
