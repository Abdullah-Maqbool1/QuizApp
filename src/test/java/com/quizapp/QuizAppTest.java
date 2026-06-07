package com.quizapp;

import com.quizapp.factory.QuestionFactory;
import com.quizapp.manager.ScoreManager;
import com.quizapp.model.MultipleChoiceQuestion;
import com.quizapp.model.Question;
import com.quizapp.model.TrueFalseQuestion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuizAppTest {

    @Test
    void shouldCreateMultipleChoiceQuestionFromLine() {
        Question question = QuestionFactory.fromLine("General|MCQ|What is 2+2?|1;2;3;4|4");

        assertTrue(question instanceof MultipleChoiceQuestion);
        assertEquals("General", question.getCategory());
        assertEquals("What is 2+2?", question.getText());
        assertEquals("4", question.getCorrectAnswer());
    }

    @Test
    void shouldCreateTrueFalseQuestionFromLine() {
        Question question = QuestionFactory.fromLine("Math|TF|Is 5 prime?||true");

        assertTrue(question instanceof TrueFalseQuestion);
        assertEquals("Math", question.getCategory());
        assertEquals("Is 5 prime?", question.getText());
        assertEquals("true", question.getCorrectAnswer());
    }

    @Test
    void questionFactoryShouldRejectMalformedLine() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> QuestionFactory.fromLine("invalid|line")
        );

        assertTrue(exception.getMessage().contains("Malformed question line"));
    }

    @Test
    void scoreManagerShouldTrackScoreAndPercentage() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.reset(4);

        manager.record(true);
        manager.record(false);
        manager.record(true);

        assertEquals(2, manager.getScore());
        assertEquals(3, manager.getAnswered());
        assertEquals(4, manager.getTotal());
        assertEquals(50, manager.getPercentage());
    }
}
