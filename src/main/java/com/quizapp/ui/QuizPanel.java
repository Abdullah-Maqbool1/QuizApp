package com.quizapp.ui;

import com.quizapp.exception.InvalidAnswerException;
import com.quizapp.manager.ScoreManager;
import com.quizapp.manager.ScoreObserver;
import com.quizapp.model.Question;
import com.quizapp.service.QuizTimer;
import com.quizapp.session.QuizSession;

import javax.swing.*;
import java.awt.*;

/**
 * Screen 3: the quiz itself ~ question text, a live countdown timer, the
 * answer options, and a "Next" button. Also a live score label that updates
 * using the "OBSERVER pattern".
 */
public class QuizPanel extends JPanel implements ScoreObserver {

    private static final int SECONDS_PER_QUESTION = 15;

    private final QuizFrame frame;

    private final JLabel progressLabel = new JLabel();
    private final JLabel scoreLabel = new JLabel();
    private final JLabel timerLabel = new JLabel();
    private final JLabel questionLabel = new JLabel();
    private final JPanel optionsPanel = new JPanel();
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JButton nextButton = new JButton("Next");

    private QuizTimer quizTimer;
    private Timer swingTimer; // drives the countdown UI
    private boolean answered;

    public QuizPanel(QuizFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(progressLabel);
        left.add(scoreLabel);

        header.add(left, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setOpaque(false);

        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new GridLayout(0, 1, 8, 8));

        body.add(questionLabel, BorderLayout.NORTH);
        body.add(optionsPanel, BorderLayout.CENTER);
        return body;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        // Event handling: Next button click.
        nextButton.addActionListener(e -> handleNext());
        footer.add(nextButton);
        return footer;
    }

    /** Called by the frame when entering this screen. */
    public void beginQuiz() {
        ScoreManager score = ScoreManager.getInstance();
        score.addObserver(this);
        score.reset(QuizSession.getInstance().getTotal());
        renderCurrentQuestion();
    }

    private void renderCurrentQuestion() {
        answered = false;
        Question q = QuizSession.getInstance().current();
        if (q == null) {
            frame.show(QuizFrame.RESULTS);
            return;
        }

        int index = QuizSession.getInstance().getCurrentIndex() + 1;
        int total = QuizSession.getInstance().getTotal();
        progressLabel.setText("Question " + index + " of " + total
                + "   (" + QuizSession.getInstance().getCategory() + ")");
        questionLabel.setText("<html><body style='width:480px'>" + q.getText() + "</body></html>");

        optionsPanel.removeAll();
        optionGroup.clearSelection();
        // Remove old radio buttons from the group.
        java.util.Enumeration<AbstractButton> existing = optionGroup.getElements();
        while (existing.hasMoreElements()) {
            optionGroup.remove(existing.nextElement());
        }
        for (String option : q.getOptions()) {
            JRadioButton rb = new JRadioButton(option);
            rb.setActionCommand(option);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 16));
            optionGroup.add(rb);
            optionsPanel.add(rb);
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();

        nextButton.setText(QuizSession.getInstance().hasNext() ? "Next" : "Finish");
        startTimer();
    }

    private void startTimer() {
        stopTimer();
        quizTimer = new QuizTimer(SECONDS_PER_QUESTION);
        quizTimer.setTimeoutListener(this::handleTimeout);
        timerLabel.setText("Time: " + quizTimer.getRemaining() + "s");

        // Event handling: the Swing timer ticks once per second.
        swingTimer = new Timer(1000, e -> {
            int remaining = quizTimer.tick();
            timerLabel.setText("Time: " + remaining + "s");
        });
        swingTimer.start();
    }

    private void stopTimer() {
        if (swingTimer != null) {
            swingTimer.stop();
            swingTimer = null;
        }
    }

    private void handleTimeout() {
        if (answered) {
            return;
        }
        stopTimer();
        // Time ran out -> count as a wrong answer and move on.
        ScoreManager.getInstance().record(false);
        answered = true;
        JOptionPane.showMessageDialog(this,
                "Time's up! The correct answer was: "
                        + QuizSession.getInstance().current().getCorrectAnswer(),
                "Timeout", JOptionPane.WARNING_MESSAGE);
        advance();
    }

    private void handleNext() {
        if (answered) {
            advance();
            return;
        }
        String selected = optionGroup.getSelection() == null
                ? null
                : optionGroup.getSelection().getActionCommand();

        Question q = QuizSession.getInstance().current();
        try {
            // Exception handling: empty answer throws InvalidAnswerException.
            boolean correct = q.isCorrect(selected);
            stopTimer();
            ScoreManager.getInstance().record(correct);
            answered = true;
            advance();
        } catch (InvalidAnswerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No Answer Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void advance() {
        if (QuizSession.getInstance().hasNext()) {
            QuizSession.getInstance().next();
            renderCurrentQuestion();
        } else {
            stopTimer();
            ScoreManager.getInstance().removeObserver(this);
            frame.show(QuizFrame.RESULTS);
        }
    }

    // OBSERVER callback: keep the live score label in sync.
    @Override
    public void onScoreChanged(int score, int answeredCount, int total) {
        scoreLabel.setText("Score: " + score + " / " + total);
    }
}
