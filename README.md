# Simple QuizApp

A small Java Swing quiz application built for a university Software Design course. It provides category selection, timed questions, and a results screen while maintaining a clean separation between the GUI and quiz logic.

## Design Patterns Implemented

* **Singleton**: `QuizManager` provides a single shared instance of `QuizLogic`.
* **Factory**: `QuestionFactory` parses input lines and creates `QuizLogic.Question` objects.
* **Builder**: `QuizLogic.QuestionBuilder` constructs questions using a fluent API.
* **Strategy**: `AnswerValidationStrategy` encapsulates answer-validation behavior.
* **Command**: `QuizGUI` uses command objects to handle button actions.

## Event Handling

* The GUI uses Swing `ActionListener` callbacks to handle button clicks.
* A Swing `Timer` updates the quiz countdown every second and automatically handles timeout transitions.

## Exception Handling

* Invalid quiz states and malformed input are handled using `IllegalArgumentException` and `IllegalStateException`.
* The GUI displays error messages using `JOptionPane` dialogs when user input is invalid or when a selected category contains no questions.

## Setup Instructions

1. Open the `QuizApp` project folder in your Java IDE (e.g., Eclipse, IntelliJ IDEA, or NetBeans).
2. Ensure JDK 8 or later is installed and configured.
3. Add JUnit 4 to the project dependencies if you want to run the unit tests.
4. Run `src/QuizGUI.java` as a Java Application to launch the quiz.

## Running Tests

### Using an IDE

Run:

```java
test/QuizLogicTest.java
```

as a JUnit test class.

### Using Maven

From the project root directory, execute:

```bash
mvn test
```

## Project Structure

| File                                       | Description                                          |
| ------------------------------------------ | ---------------------------------------------------- |
| `src/QuizGUI.java`                         | Main GUI and application flow                        |
| `src/QuizLogic.java`                       | Question loading, scoring, and quiz state management |
| `src/QuizManager.java`                     | Singleton quiz manager                               |
| `src/QuestionFactory.java`                 | Factory for parsing question data                    |
| `src/AnswerValidationStrategy.java`        | Strategy interface for answer validation             |
| `src/DefaultAnswerValidationStrategy.java` | Default answer validation implementation             |
| `src/questions.txt`                        | Sample questions loaded at runtime                   |
| `test/QuizLogicTest.java`                  | Unit tests for quiz logic                            |


## Screenshot

