package com.quizapp.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Screen 1: Home. Welcome message and a Start button.
 */
public class HomePanel extends JPanel {

    public HomePanel(QuizFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(new Color(0x10, 0x16, 0x2A));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel title = new JLabel("Quiz Master");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(new Color(0x4F, 0x9C, 0xFF));

        JLabel subtitle = new JLabel("Pick a category, beat the clock, ace the quiz!");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(Color.LIGHT_GRAY);

        JButton start = new JButton("Start Quiz");
        start.setFont(new Font("SansSerif", Font.BOLD, 18));
        start.setFocusPainted(false);
        start.setBackground(new Color(0x4F, 0x9C, 0xFF));
        start.setForeground(Color.WHITE);
        start.setPreferredSize(new Dimension(200, 50));
        // Event handling: button click navigates to the category screen.
        start.addActionListener(e -> frame.show(QuizFrame.CATEGORY));

        gbc.gridy = 0;
        add(title, gbc);
        gbc.gridy = 1;
        add(subtitle, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 12, 12, 12);
        add(start, gbc);
    }
}
