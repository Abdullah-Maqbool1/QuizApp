package com.quizapp.model;

import com.quizapp.exception.InvalidAnswerException;

import java.util.List;

/**
 * Abstract base for all question types.
 *
 * Design notes:
 *  - SRP: a Question only knows its own data and how to validate an answer.
 *  - DRY: answer validation lives here once and is reused by every subclass.
 *  - Open/Closed: new question types extend this class without changing it.
 */
public abstract class Question {

    private final String category;
    private final String text;
    private final String correctAnswer;

    protected Question(String category, String text, String correctAnswer) {
        this.category = category;
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    /** Options presented to the user (e.g. 4 choices, or True/False). */
    public abstract List<String> getOptions();

    /** Short label for the question type, used by the factory & UI. */
    public abstract String getType();

    /**
     * Single, shared validation path (DRY).
     * @throws InvalidAnswerException if the answer is null/empty.
     */
    public boolean isCorrect(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new InvalidAnswerException("Answer cannot be empty. Please select an option.");
        }
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }

    public String getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
