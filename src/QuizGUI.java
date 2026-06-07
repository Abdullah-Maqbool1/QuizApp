import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * QuizGUI — simple Swing GUI that uses QuizLogic for quiz data and scoring.
 * Two main responsibilities: present screens and forward user actions to QuizLogic.
 */
public class QuizGUI extends JFrame {

    private QuizLogic logic = QuizManager.getInstance().getLogic();
    private JPanel cards;
    private final static String HOME = "HOME";
    private final static String CATS = "CATS";
    private final static String QUIZ = "QUIZ";
    private final static String RESULTS = "RESULTS";

    private interface Command {
        void execute();
    }

    private class ShowCardCommand implements Command {
        private final String cardName;

        private ShowCardCommand(String cardName) {
            this.cardName = cardName;
        }

        @Override
        public void execute() {
            showCard(cardName);
        }
    }

    private class StartCategoryCommand implements Command {
        private final String category;

        private StartCategoryCommand(String category) {
            this.category = category;
        }

        @Override
        public void execute() {
            startCategory(category);
        }
    }

    private class NextQuestionCommand implements Command {
        @Override
        public void execute() {
            onNext();
        }
    }

    // Quiz screen components
    private JLabel questionLabel;
    private JRadioButton[] optionButtons = new JRadioButton[4];
    private ButtonGroup optionsGroup = new ButtonGroup();
    private JLabel timerLabel;
    private Timer swingTimer;
    private int timeRemaining;

    public QuizGUI() {
        setTitle("Simple QuizApp");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        cards = new JPanel(new CardLayout());
        cards.add(homePanel(), HOME);
        cards.add(categoryPanel(), CATS);
        cards.add(quizPanel(), QUIZ);
        cards.add(resultsPanel(), RESULTS);
        add(cards);
    }

    private JPanel homePanel() {
        JPanel p = new JPanel(null);
        JLabel title = new JLabel("Welcome to Simple QuizApp");
        title.setBounds(90, 40, 300, 30);
        p.add(title);
        JButton start = new JButton("Choose Category");
        start.setBounds(170, 120, 140, 30);
        start.addActionListener(e -> new ShowCardCommand(CATS).execute());
        p.add(start);
        return p;
    }

    private JPanel categoryPanel() {
        JPanel p = new JPanel(null);
        JLabel label = new JLabel("Select a category:");
        label.setBounds(30, 20, 200, 25);
        p.add(label);
        int x = 30, y = 60;
        for (String cat : logic.getCategories()) {
            JButton b = new JButton(cat);
            b.setBounds(x, y, 120, 30);
            b.addActionListener(e -> new StartCategoryCommand(cat).execute());
            p.add(b);
            y += 45;
        }
        JButton back = new JButton("Back");
        back.setBounds(380, 250, 80, 25);
        back.addActionListener(e -> showCard(HOME));
        p.add(back);
        return p;
    }

    private JPanel quizPanel() {
        JPanel p = new JPanel(null);
        questionLabel = new JLabel("Question text");
        questionLabel.setBounds(20, 10, 440, 40);
        p.add(questionLabel);
        int y = 60;
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setBounds(30, y, 420, 30);
            optionsGroup.add(optionButtons[i]);
            p.add(optionButtons[i]);
            y += 40;
        }
        timerLabel = new JLabel("Time: --");
        timerLabel.setBounds(20, 230, 120, 25);
        p.add(timerLabel);
        JButton next = new JButton("Next");
        next.setBounds(370, 230, 90, 30);
        next.addActionListener(e -> new NextQuestionCommand().execute());
        p.add(next);
        return p;
    }

    private JPanel resultsPanel() {
        JPanel p = new JPanel(null);
        JLabel res = new JLabel("Results");
        res.setBounds(190, 40, 200, 30);
        p.add(res);
        JLabel scoreLabel = new JLabel();
        scoreLabel.setBounds(100, 100, 300, 30);
        p.add(scoreLabel);
        JButton restart = new JButton("Restart");
        restart.setBounds(200, 180, 90, 30);
        restart.addActionListener(e -> {
            logic.reset();
            showCard(HOME);
        });
        p.add(restart);

        // When results card is shown, update score text
        p.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                scoreLabel.setText("Score: " + logic.getScore() + " / " + logic.getTotalQuestions());
            }
        });

        return p;
    }

    private void startCategory(String cat) {
        try {
            logic.selectCategory(cat);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showCard(QUIZ);
        loadQuestion();
    }

    private void loadQuestion() {
        QuizLogic.Question q = logic.getCurrentQuestion();
        if (q == null) { // no more questions
            showCard(RESULTS);
            return;
        }
        questionLabel.setText((logic.getCurrentIndex() + 1) + ". " + q.text);
        for (int i = 0; i < optionButtons.length; i++) {
            if (i < q.options.length) {
                optionButtons[i].setText(q.options[i]);
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }
        optionsGroup.clearSelection();
        // setup timer (limit to 15 seconds maximum)
        if (swingTimer != null && swingTimer.isRunning()) swingTimer.stop();
        timeRemaining = Math.min(q.timeSeconds, 15);
        timerLabel.setText("Time: " + timeRemaining + "s");
        swingTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Time: " + timeRemaining + "s");
                if (timeRemaining <= 0) {
                    swingTimer.stop();
                    JOptionPane.showMessageDialog(QuizGUI.this,
                            "Time is up! This answer will be considered wrong.",
                            "Time Expired",
                            JOptionPane.ERROR_MESSAGE);
                    logic.nextQuestion();
                    loadQuestion();
                }
            }
        });
        swingTimer.start();
    }

    private void onNext() {
        // find selected option
        int selected = -1;
        for (int i = 0; i < optionButtons.length; i++) if (optionButtons[i].isVisible() && optionButtons[i].isSelected()) selected = i;
        try {
            if (selected >= 0) {
                logic.submitAnswer(selected);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (swingTimer != null && swingTimer.isRunning()) swingTimer.stop();
        logic.nextQuestion();
        loadQuestion();
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizGUI gui = new QuizGUI();
            gui.setVisible(true);
        });
    }
}
