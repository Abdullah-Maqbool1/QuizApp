public class DefaultAnswerValidationStrategy implements AnswerValidationStrategy {

    @Override
    public boolean isCorrect(QuizLogic.Question question, int selectedIndex) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        if (selectedIndex < 0 || selectedIndex >= question.options.length) {
            throw new IllegalArgumentException("Invalid answer selected");
        }
        return question.options[selectedIndex].equalsIgnoreCase(question.correctAnswer);
    }
}
