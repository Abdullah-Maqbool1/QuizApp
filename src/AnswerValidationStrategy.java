public interface AnswerValidationStrategy {
    boolean isCorrect(QuizLogic.Question question, int selectedIndex);
}
