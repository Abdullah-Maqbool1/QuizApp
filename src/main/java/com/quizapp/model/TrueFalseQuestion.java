package com.quizapp.model;

import java.util.Arrays;
import java.util.List;

/**
 * A true/false question. Options are fixed.
 */
public class TrueFalseQuestion extends Question {

    public static final String TYPE = "TF";

    public TrueFalseQuestion(String category, String text, String correctAnswer) {
        super(category, text, correctAnswer);
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList("True", "False");
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
