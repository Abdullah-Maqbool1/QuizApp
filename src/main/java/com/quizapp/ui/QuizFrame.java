package com.quizapp.ui;

import com.quizapp.service.QuestionLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window. Uses a CardLayout to switch between the 4 screens:
 * Home -> Category -> Quiz -> Results.
 *
 * SRP: owns navigation between panels only.
 */
public class QuizFrame extends JFrame {

    public static final String HOME = "home";
    public static final String CATEGORY = "category";
    public static final String QUIZ = "quiz";
    public static final String RESULTS = "results";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    private final QuestionLoader loader = new QuestionLoader();

    private HomePanel homePanel;
    private CategoryPanel categoryPanel;
    private QuizPanel quizPanel;
    private ResultsPanel resultsPanel;

    public QuizFrame() {
        setTitle("Quiz App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 520);
        setMinimumSize(new Dimension(560, 460));
        setLocationRelativeTo(null);

        try {
            loader.load();
        } catch (Exception e) {
            // Exception handling at startup: show a friendly dialog and exit.
            JOptionPane.showMessageDialog(this,
                    "Could not load questions:\n" + e.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
        }

        homePanel = new HomePanel(this);
        categoryPanel = new CategoryPanel(this, loader);
        quizPanel = new QuizPanel(this);
        resultsPanel = new ResultsPanel(this);

        container.add(homePanel, HOME);
        container.add(categoryPanel, CATEGORY);
        container.add(quizPanel, QUIZ);
        container.add(resultsPanel, RESULTS);

        add(container);
        show(HOME);
    }

    /** Navigate to a named screen and let it refresh its state. */
    public void show(String name) {
        switch (name) {
            case CATEGORY:
                categoryPanel.refresh();
                break;
            case QUIZ:
                quizPanel.beginQuiz();
                break;
            case RESULTS:
                resultsPanel.refresh();
                break;
            default:
                break;
        }
        cardLayout.show(container, name);
    }
}
