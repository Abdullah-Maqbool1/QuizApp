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

    private List<Question> allQuestions = new ArrayList<>();
    private List<Question> currentQuestions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    public QuizLogic() {
        loadQuestionsFromFile("questions.txt");
    }

    private void loadDefaultQuestions() {
        allQuestions.clear();
        allQuestions.add(new Question("Math", "MCQ", "What is 2+2?", new String[]{"3","4","5","6"}, "4", 20));
        allQuestions.add(new Question("Math", "MCQ", "What is 5*3?", new String[]{"8","15","10","5"}, "15", 20));
        allQuestions.add(new Question("Science", "MCQ", "Water's chemical formula?", new String[]{"H2O","CO2","O2","NaCl"}, "H2O", 20));
        allQuestions.add(new Question("General", "MCQ", "The sky is usually what color?", new String[]{"Blue","Green","Red","Yellow"}, "Blue", 15));
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
                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) continue; // skip malformed
                String category = parts[0].trim();
                String type = parts[1].trim();
                String text = parts[2].trim();
                String optsField = parts[3].trim();
                String correct = parts[4].trim();
                String[] opts;
                if (type.equalsIgnoreCase("TF")) {
                    opts = new String[]{"True", "False"};
                } else {
                    if (optsField.isEmpty()) opts = new String[0];
                    else {
                        opts = optsField.split(";");
                        for (int i = 0; i < opts.length; i++) opts[i] = opts[i].trim();
                    }
                }
                int timeSec = 30;
                allQuestions.add(new Question(category, type, text, opts, correct, timeSec));
            }
        } catch (IOException e) {
            // If file not found or error, fall back to built-in default questions.
            System.err.println("Failed to load questions from " + path + ": " + e.getMessage());
            loadDefaultQuestions();
            return;
        }
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
        if (selectedIndex < 0 || selectedIndex >= q.options.length) throw new IllegalArgumentException("Invalid answer selected");
        String selectedText = q.options[selectedIndex];
        boolean correct = selectedText.equalsIgnoreCase(q.correctAnswer);
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

