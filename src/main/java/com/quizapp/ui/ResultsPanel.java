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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Results");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        percentLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton home = new JButton("Back to Home");
        home.setFont(new Font("SansSerif", Font.BOLD, 16));
        home.setAlignmentX(Component.CENTER_ALIGNMENT);
        home.addActionListener(e -> frame.show(QuizFrame.HOME));

        add(title);
        add(Box.createRigidArea(new Dimension(0, 12)));
        add(scoreLabel);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(percentLabel);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(messageLabel);
        add(Box.createRigidArea(new Dimension(0, 28)));
        add(home);
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
