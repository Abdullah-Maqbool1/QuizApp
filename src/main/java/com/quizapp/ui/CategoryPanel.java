package com.quizapp.ui;

import com.quizapp.model.Question;
import com.quizapp.service.QuestionLoader;
import com.quizapp.session.QuizSession;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Screen 2: Category picker. One button per available category.
 */
public class CategoryPanel extends JPanel {

    private final QuizFrame frame;
    private final QuestionLoader loader;
    private final JPanel buttons = new JPanel();

    public CategoryPanel(QuizFrame frame, QuestionLoader loader) {
        this.frame = frame;
        this.loader = loader;

        setLayout(new BorderLayout());

        JLabel header = new JLabel("Choose a Category", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setBorder(BorderFactory.createEmptyBorder(24, 0, 12, 0));
        add(header, BorderLayout.NORTH);

        buttons.setLayout(new GridLayout(0, 1, 12, 12));
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(buttons);
        add(wrapper, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> frame.show(QuizFrame.HOME));
        JPanel south = new JPanel();
        south.add(back);
        add(south, BorderLayout.SOUTH);
    }

    /** Rebuilds the category buttons from the loaded data. */
    public void refresh() {
        buttons.removeAll();
        for (String category : loader.getCategories()) {
            JButton b = new JButton(category);
            b.setFont(new Font("SansSerif", Font.PLAIN, 18));
            b.setPreferredSize(new Dimension(260, 44));
            // Event handling: choose category -> start session -> go to quiz.
            b.addActionListener(e -> startCategory(category));
            buttons.add(b);
        }
        buttons.revalidate();
        buttons.repaint();
    }

    private void startCategory(String category) {
        List<Question> questions = loader.byCategory(category);
        QuizSession.getInstance().start(category, questions);
        frame.show(QuizFrame.QUIZ);
    }
}
