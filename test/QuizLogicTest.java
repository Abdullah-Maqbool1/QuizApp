import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

public class QuizLogicTest {

    @Test
    public void testLoadAndCategories() throws IOException {
        // create a small temporary questions file
        String tempPath = "test_temp_questions.txt";
        try (FileWriter fw = new FileWriter(tempPath)) {
            fw.write("TST|Q1?|A;B;C;D|0|10\n");
        }
        QuizLogic logic = new QuizLogic();
        logic.loadQuestionsFromFile(tempPath);
        Assertions.assertTrue(logic.getCategories().contains("TST"));
    }

    @Test
    public void testScoringFlow() throws IOException {
        String tempPath = "test_temp_questions2.txt";
        try (FileWriter fw = new FileWriter(tempPath)) {
            fw.write("Math|MCQ|What is 2+2?|1;2;3;4|4|20\n");
        }
        QuizLogic logic = new QuizLogic();
        logic.loadQuestionsFromFile(tempPath);
        logic.selectCategory("Math");
        QuizLogic.Question q = logic.getCurrentQuestion();
        Assertions.assertNotNull(q);
        int correctIndex = -1;
        for (int i = 0; i < q.options.length; i++) {
            if (q.options[i].equals(q.correctAnswer)) {
                correctIndex = i;
                break;
            }
        }
        Assertions.assertTrue(correctIndex >= 0, "Correct answer must appear in options");
        boolean ok = logic.submitAnswer(correctIndex);
        Assertions.assertTrue(ok);
        logic.nextQuestion();
        Assertions.assertEquals(1, logic.getScore());
    }
}
