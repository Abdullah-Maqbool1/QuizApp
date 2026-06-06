package com.quizapp;

import com.quizapp.ui.QuizFrame;

import javax.swing.SwingUtilities;

/**
 * Application entry point. Launches the Swing UI on the Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new QuizFrame().setVisible(true));
    }
}
