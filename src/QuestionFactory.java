public class QuestionFactory {

    public static QuizLogic.Question createQuestion(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 5) {
            return null;
        }

        String category = parts[0].trim();
        String type = parts[1].trim();
        String text = parts[2].trim();
        String optsField = parts[3].trim();
        String correctAnswer = parts[4].trim();
        int timeSeconds = 30;

        QuizLogic.QuestionBuilder builder = new QuizLogic.QuestionBuilder()
                .category(category)
                .type(type)
                .text(text)
                .correctAnswer(correctAnswer)
                .timeSeconds(timeSeconds);

        if (type.equalsIgnoreCase("TF")) {
            builder.options(new String[]{"True", "False"});
        } else {
            String[] options = optsField.isEmpty() ? new String[0] : optsField.split(";");
            for (int i = 0; i < options.length; i++) {
                options[i] = options[i].trim();
            }
            builder.options(options);
        }

        return builder.build();
    }
}
