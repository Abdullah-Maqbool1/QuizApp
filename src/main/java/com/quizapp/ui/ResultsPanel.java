package com.quizapp.ui;

import com.quizapp.manager.ScoreManager;

import javax.swing.*;
import java.awt.*;

/**
 * Screen 4: Results. Shows final score, percentage, and a short review message.
 */
public class ResultsPanel extends JPanel {

    private final JLabel scoreLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel percentLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);

    public ResultsPanel(QuizFrame frame) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Results");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));

        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        percentLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));

        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));

        JButton home = new JButton("Back to Home");
        home.setFont(new Font("SansSerif", Font.BOLD, 16));
        home.addActionListener(e -> frame.show(QuizFrame.HOME));

        gbc.gridy = 0;
        add(title, gbc);
        gbc.gridy = 1;
        add(scoreLabel, gbc);
        gbc.gridy = 2;
        add(percentLabel, gbc);
        gbc.gridy = 3;
        add(messageLabel, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(28, 10, 10, 10);
        add(home, gbc);
    }

    /** Pulls final numbers from the ScoreManager singleton. */
    public void refresh() {
        ScoreManager score = ScoreManager.getInstance();
        scoreLabel.setText("You scored " + score.getScore() + " out of " + score.getTotal());
        int pct = score.getPercentage();
        percentLabel.setText(pct + "%");
        messageLabel.setText(reviewMessage(pct));
    }

    private String reviewMessage(int pct) {
        if (pct >= 80) {
            return "Outstanding! You really know your stuff.";
        } else if (pct >= 50) {
            return "Good effort - a little more practice and you'll ace it.";
        }
        return "Keep studying - you'll get there!";
    }
}
