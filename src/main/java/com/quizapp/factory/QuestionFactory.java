package com.quizapp.factory;

import com.quizapp.model.MultipleChoiceQuestion;
import com.quizapp.model.Question;
import com.quizapp.model.TrueFalseQuestion;

import java.util.Arrays;
import java.util.List;

/**
 * FACTORY PATTERN.

 * Centralises creation of {@link Question} objects so callers never use
 * "new MultipleChoiceQuestion(...)" directly. Adding a new question type
 * means editing only this class.

 * Expected line format (pipe-separated):
 *   category|type|text|opt1;opt2;opt3;opt4|correctAnswer
 *   - For TF, the options field may be left empty.
 */
public final class QuestionFactory {

    private QuestionFactory() {
        // utility class - no instances
    }

    public static Question fromLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Question line is empty.");
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Malformed question line: " + line);
        }

        String category = parts[0].trim();
        String type = parts[1].trim().toUpperCase();
        String text = parts[2].trim();
        String optionsRaw = parts[3].trim();
        String answer = parts[4].trim();

        switch (type) {
            case MultipleChoiceQuestion.TYPE:
                List<String> options = Arrays.asList(optionsRaw.split(";"));
                return new MultipleChoiceQuestion(category, text, options, answer);
            case TrueFalseQuestion.TYPE:
                return new TrueFalseQuestion(category, text, answer);
            default:
                throw new IllegalArgumentException("Unknown question type: " + type);
        }
    }
}
