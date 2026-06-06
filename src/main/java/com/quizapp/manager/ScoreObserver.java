package com.quizapp.manager;

/**
 * OBSERVER PATTERN - observer contract.
 * Implemented by UI components that want live score updates.
 */
public interface ScoreObserver {
    void onScoreChanged(int score, int answered, int total);
}
