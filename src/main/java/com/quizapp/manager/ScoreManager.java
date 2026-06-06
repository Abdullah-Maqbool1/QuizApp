package com.quizapp.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON + OBSERVER (subject).
 *
 * - Singleton: a single source of truth for the running score.
 * - Observer: notifies registered {@link ScoreObserver}s whenever the score
 *   or progress changes, so the UI updates live.
 *
 * SRP: this class only tracks score/progress and broadcasts changes.
 */
public final class ScoreManager {

    private static final ScoreManager INSTANCE = new ScoreManager();

    private final List<ScoreObserver> observers = new ArrayList<>();
    private int score;
    private int answered;
    private int total;

    private ScoreManager() {
    }

    public static ScoreManager getInstance() {
        return INSTANCE;
    }

    public void reset(int total) {
        this.score = 0;
        this.answered = 0;
        this.total = total;
        notifyObservers();
    }

    /** Records an answer and updates the score. */
    public void record(boolean correct) {
        answered++;
        if (correct) {
            score++;
        }
        notifyObservers();
    }

    public void addObserver(ScoreObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ScoreObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ScoreObserver o : observers) {
            o.onScoreChanged(score, answered, total);
        }
    }

    public int getScore() {
        return score;
    }

    public int getAnswered() {
        return answered;
    }

    public int getTotal() {
        return total;
    }

    public int getPercentage() {
        return total == 0 ? 0 : Math.round((score * 100f) / total);
    }
}
