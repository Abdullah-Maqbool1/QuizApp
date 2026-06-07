import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// quiz backend logic. Responsibilities: load questions, manage quiz state, validate answers, calculate score.
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

    private BufferedReader openQuestionReader(String path) throws IOException {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new BufferedReader(new FileReader(file));
        }
        if ("questions.txt".equals(path)) {
            java.io.File alt = new java.io.File("src/questions.txt");
            if (alt.exists()) {
                return new BufferedReader(new FileReader(alt));
            }
            java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("questions.txt");
            if (resourceStream != null) {
                return new BufferedReader(new java.io.InputStreamReader(resourceStream));
            }
        }
        throw new IOException("File not found: " + path);
    }

    /** Load questions from a simple text file. Format per line:
     * category|type|text|opt1;opt2;opt3;opt4|correctAnswer
     * type is MCQ or TF. For TF the options field is empty and correctAnswer is True/False.
     */
    public final void loadQuestionsFromFile(String path) {
        allQuestions.clear();
        try (BufferedReader br = openQuestionReader(path)) {
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

