public class QuizManager {

    private static final QuizManager INSTANCE = new QuizManager();
    private final QuizLogic logic;

    private QuizManager() {
        logic = new QuizLogic();
    }

    public static QuizManager getInstance() {
        return INSTANCE;
    }

    public QuizLogic getLogic() {
        return logic;
    }
}
