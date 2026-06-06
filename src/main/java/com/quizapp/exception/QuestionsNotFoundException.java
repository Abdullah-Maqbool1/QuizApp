package com.quizapp.exception;

/**
 * Thrown when the questions data file cannot be located or read.
 * Demonstrates custom (checked) exception handling.
 */
public class QuestionsNotFoundException extends Exception {
    public QuestionsNotFoundException(String message) {
        super(message);
    }

    public QuestionsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
