import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * QuizLogic — simple quiz backend for the simplified QuizApp.
 * Keeps questions in memory, tracks current question and score.
 */
public class QuizLogic {

    public static class Question {
        public String category;
        public String type; // MCQ or TF
        public String text;
        public String[] options;
        public String correctAnswer; // exact text of the correct answer
        public int timeSeconds;

        public Question(String category, String type, String text, String[] options, String correctAnswer, int timeSeconds) {
            this.category = category;
            this.type = type;
            this.text = text;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.timeSeconds = timeSeconds;
        }
    }

    public static class QuestionBuilder {
        private String category = "";
        private String type = "MCQ";
        private String text = "";
        private String[] options = new String[0];
        private String correctAnswer = "";
        private int timeSeconds = 30;

        public QuestionBuilder category(String category) {
            this.category = category;
            return this;
        }

        public QuestionBuilder type(String type) {
            this.type = type;
            return this;
        }

        public QuestionBuilder text(String text) {
            this.text = text;
            return this;
        }

        public QuestionBuilder options(String[] options) {
            this.options = options;
            return this;
        }

        public QuestionBuilder correctAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public QuestionBuilder timeSeconds(int timeSeconds) {
            this.timeSeconds = timeSeconds;
            return this;
        }

        public Question build() {
            return new Question(category, type, text, options, correctAnswer, timeSeconds);
        }
    }

    private AnswerValidationStrategy validationStrategy = new DefaultAnswerValidationStrategy();
    private List<Question> allQuestions = new ArrayList<>();
    private List<Question> currentQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    public QuizLogic() {
        loadQuestionsFromFile("questions.txt");
    }

    private void loadDefaultQuestions() {
        allQuestions.clear();
        allQuestions.add(new QuestionBuilder()
                .category("Math")
                .type("MCQ")
                .text("What is 2+2?")
                .options(new String[]{"3", "4", "5", "6"})
                .correctAnswer("4")
                .timeSeconds(20)
                .build());
        allQuestions.add(new QuestionBuilder()
                .category("Math")
                .type("MCQ")
                .text("What is 5*3?")
                .options(new String[]{"8", "15", "10", "5"})
                .correctAnswer("15")
                .timeSeconds(20)
                .build());
        allQuestions.add(new QuestionBuilder()
                .category("Science")
                .type("MCQ")
                .text("Water's chemical formula?")
                .options(new String[]{"H2O", "CO2", "O2", "NaCl"})
                .correctAnswer("H2O")
                .timeSeconds(20)
                .build());
        allQuestions.add(new QuestionBuilder()
                .category("General")
                .type("MCQ")
                .text("The sky is usually what color?")
                .options(new String[]{"Blue", "Green", "Red", "Yellow"})
                .correctAnswer("Blue")
                .timeSeconds(15)
                .build());
    }

    /** Load questions from a simple text file. Format per line:
     * category|type|text|opt1;opt2;opt3;opt4|correctAnswer
     * type is MCQ or TF. For TF the options field is empty and correctAnswer is True/False.
     */
    public final void loadQuestionsFromFile(String path) {
        allQuestions.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                QuizLogic.Question question = QuestionFactory.createQuestion(line);
                if (question != null) {
                    allQuestions.add(question);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load questions from " + path + ": " + e.getMessage());
            loadDefaultQuestions();
            return;
        }
    }

    public void setValidationStrategy(AnswerValidationStrategy validationStrategy) {
        if (validationStrategy == null) {
            throw new IllegalArgumentException("Validation strategy cannot be null");
        }
        this.validationStrategy = validationStrategy;
    }

    public Set<String> getCategories() {
        Set<String> cats = new HashSet<>();
        for (Question q : allQuestions) cats.add(q.category);
        return cats;
    }

    public void selectCategory(String category) {
        currentQuestions.clear();
        for (Question q : allQuestions) if (q.category.equals(category)) currentQuestions.add(q);
        if (currentQuestions.isEmpty()) throw new IllegalArgumentException("No questions for category: " + category);
        currentIndex = 0;
        score = 0;
    }

    public Question getCurrentQuestion() {
        if (currentIndex < 0 || currentIndex >= currentQuestions.size()) return null;
        return currentQuestions.get(currentIndex);
    }

    public boolean submitAnswer(int selectedIndex) {
        Question q = getCurrentQuestion();
        if (q == null) throw new IllegalStateException("No current question");
        boolean correct = validationStrategy.isCorrect(q, selectedIndex);
        if (correct) score++;
        return correct;
    }

    public void nextQuestion() {
        currentIndex++;
    }

    public int getScore() { return score; }

    public int getTotalQuestions() { return currentQuestions.size(); }

    public int getCurrentIndex() { return currentIndex; }

    public void reset() { currentQuestions.clear(); currentIndex = 0; score = 0; }
}

