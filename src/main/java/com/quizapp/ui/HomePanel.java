package com.quizapp.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Screen 1: Home. Welcome message and a Start button.
 */
public class HomePanel extends JPanel {

    public HomePanel(QuizFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Quiz Master");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Pick a category, beat the clock, ace the quiz!");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = new JButton("Start Quiz");
        start.setFont(new Font("SansSerif", Font.BOLD, 18));
        start.setPreferredSize(new Dimension(200, 50));
        start.setMaximumSize(new Dimension(200, 50));
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> frame.show(QuizFrame.CATEGORY));

        add(title);
        add(Box.createRigidArea(new Dimension(0, 12)));
        add(subtitle);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(start);
    }
}
