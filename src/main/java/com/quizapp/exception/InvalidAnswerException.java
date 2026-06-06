package com.quizapp.exception;

/**
 * Thrown when a user submits an empty or invalid answer.
 * Demonstrates runtime exception handling for input validation.
 */
public class InvalidAnswerException extends RuntimeException {
    public InvalidAnswerException(String message) {
        super(message);
    }
}
