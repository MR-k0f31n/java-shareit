package ru.practicum.shareit.exception;

/**
 * @author MR.k0F31n
 */
public class UnsupportedStatus extends IllegalStateException {
    public UnsupportedStatus(String message) {
        super(message);
    }
}

