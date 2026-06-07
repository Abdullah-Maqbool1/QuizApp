# Simple QuizApp
A small Java Swing quiz application built for a university software design course. It shows category selection, timed questions, and a results screen with clean separation between GUI and quiz logic.

## Design Patterns Implemented
- **Singleton**: `QuizManager` provides a single shared `QuizLogic` instance.
- **Factory**: `QuestionFactory` parses input lines and builds `QuizLogic.Question` objects.
- **Builder**: `QuizLogic.QuestionBuilder` constructs questions with a fluent API.
- **Strategy**: `AnswerValidationStrategy` isolates the answer-checking behavior.
- **Command**: `QuizGUI` uses small command objects for button actions.

## Event Handling
- The GUI uses Swing `ActionListener` callbacks for button clicks.
- A Swing `Timer` updates the quiz countdown each second, handling timeout transitions automatically.

## Exception Handling
- Invalid quiz states and bad input are handled using `IllegalArgumentException` and `IllegalStateException`.
- The GUI shows error dialogs with `JOptionPane` when the user input is invalid or a category has no questions.

## Setup Instructions
1. Open the project folder `QuizApp` in your Java IDE or Eclipse.
2. Ensure JDK 8+ is available on the classpath.
3. Add JUnit 4 if you want to run the tests.
4. Run `src/QuizGUI.java` as a Java Application to launch the quiz UI.

### Run tests
- Run `test/QuizLogicTest.java` as a JUnit test class.
- Or use Maven from the project root:
  ```
mvn test
```

## Files
- `src/QuizGUI.java` — main GUI and application flow.
- `src/QuizLogic.java` — question loading, scoring, and quiz state.
- `src/QuizManager.java` — singleton quiz manager.
- `src/QuestionFactory.java` — factory for parsing question data.
- `src/AnswerValidationStrategy.java` — strategy interface.
- `src/DefaultAnswerValidationStrategy.java` — default validation logic.
- `src/questions.txt` — sample questions loaded at runtime.
- `test/QuizLogicTest.java` — basic unit tests.

## Questions file format
Each non-empty line uses this pipe-separated format:
```
category|type|question|opt1;opt2;opt3;opt4|correctAnswer
```
For true/false questions, use `TF` as the type and leave the options field blank.
Lines starting with `#` are ignored as comments.

## Screenshot
Add a screenshot of the running quiz UI here, for example `screenshot.png` or a screen capture image.

