package com.quizapp.model;

import java.util.Collections;
import java.util.List;

/**
 * A multiple-choice question with several options.
 */
public class MultipleChoiceQuestion extends Question {

    public static final String TYPE = "MCQ";

    private final List<String> options;

    public MultipleChoiceQuestion(String category, String text, List<String> options, String correctAnswer) {
        super(category, text, correctAnswer);
        this.options = options;
    }

    @Override
    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
