package com.quizapp.service;

/**
 * A simple countdown timer (framework independent so it is unit-testable).
 *
 * It counts down once per {@link #tick()} call. The UI drives ticks with a
 * javax.swing.Timer; tests drive ticks manually. SRP: only timing logic.
 */
public class QuizTimer {

    /** Callback fired when the timer reaches zero. */
    public interface TimeoutListener {
        void onTimeout();
    }

    private final int startSeconds;
    private int remaining;
    private boolean expired;
    private TimeoutListener listener;

    public QuizTimer(int startSeconds) {
        if (startSeconds <= 0) {
            throw new IllegalArgumentException("Timer must start above zero.");
        }
        this.startSeconds = startSeconds;
        this.remaining = startSeconds;
    }

    public void setTimeoutListener(TimeoutListener listener) {
        this.listener = listener;
    }

    /** Resets the timer back to its starting value. */
    public void reset() {
        this.remaining = startSeconds;
        this.expired = false;
    }

    /**
     * Advances the timer by one second.
     * @return remaining seconds after the tick (never below zero).
     */
    public int tick() {
        if (expired) {
            return 0;
        }
        remaining--;
        if (remaining <= 0) {
            remaining = 0;
            expired = true;
            if (listener != null) {
                listener.onTimeout();
            }
        }
        return remaining;
    }

    public int getRemaining() {
        return remaining;
    }

    public boolean isExpired() {
        return expired;
    }
}
