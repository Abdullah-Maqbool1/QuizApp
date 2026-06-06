package com.quizapp.session;

import com.quizapp.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON.
 *
 * Holds the state of the currently running quiz: the selected category,
 * the working list of questions, and the current index. A single session
 * exists at a time, so a Singleton is appropriate.
 */
public final class QuizSession {

    private static final QuizSession INSTANCE = new QuizSession();

    private String category;
    private final List<Question> questions = new ArrayList<>();
    private int currentIndex;

    private QuizSession() {
    }

    public static QuizSession getInstance() {
        return INSTANCE;
    }

    public void start(String category, List<Question> questions) {
        this.category = category;
        this.questions.clear();
        this.questions.addAll(questions);
        this.currentIndex = 0;
    }

    public boolean hasNext() {
        return currentIndex < questions.size() - 1;
    }

    public Question current() {
        if (questions.isEmpty()) {
            return null;
        }
        return questions.get(currentIndex);
    }

    public Question next() {
        if (hasNext()) {
            currentIndex++;
        }
        return current();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTotal() {
        return questions.size();
    }

    public String getCategory() {
        return category;
    }
}
