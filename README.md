# Quiz App (Java Swing)

A desktop quiz application: pick a category, answer timed multiple-choice and
true/false questions, and review your final score.

## Screens
1. **Home** – welcome + Start button
2. **Category Picker** – one button per category (Science, History, Geography, Technology)
3. **Quiz** – question text, a live countdown timer, answer options, live score, Next button
4. **Results** – final score, percentage, and a review message

## How requirements are covered

| Requirement | Where it lives |
|---|---|
| **Event Handling** | `ActionListener`s on answer/Next/Start buttons and the per-second `javax.swing.Timer` in `QuizPanel`, `HomePanel`, `CategoryPanel` |
| **Exception Handling** | `QuestionsNotFoundException` (file missing) + `InvalidAnswerException` (empty answer), handled with dialogs in `QuizFrame`/`QuizPanel` |
| **Code Refactoring** | `Question` abstract base (shared validation = DRY); each class has one job (SRP); factory removes `new` duplication |
| **Unit Testing** | JUnit 5 tests: `ScoreManagerTest`, `QuizTimerTest`, `ValidationTest`, `SessionTest` |
| **Design Patterns** | **Factory** (`QuestionFactory`), **Singleton** (`ScoreManager`, `QuizSession`), **Observer** (`ScoreObserver` → live score label) |
| **Git** | Suggested commit-per-feature flow below |

## Project layout
```
src/main/java/com/quizapp/
  Main.java
  model/      Question (abstract), MultipleChoiceQuestion, TrueFalseQuestion
  factory/    QuestionFactory            (Factory pattern)
  manager/    ScoreManager, ScoreObserver (Singleton + Observer)
  session/    QuizSession                (Singleton)
  service/    QuestionLoader, QuizTimer
  exception/  QuestionsNotFoundException, InvalidAnswerException
  ui/         QuizFrame, HomePanel, CategoryPanel, QuizPanel, ResultsPanel
src/main/resources/questions.txt
src/test/java/com/quizapp/  (JUnit 5 tests)
```

## Run it

### With Maven (recommended)
```bash
mvn test          # run the unit tests
mvn package       # build target/quiz-app-1.0.0.jar
java -jar target/quiz-app-1.0.0.jar
```

### Without Maven (plain javac)
```bash
# compile
javac -d out $(find src/main/java -name "*.java")
# copy the questions file next to the classes
cp src/main/resources/questions.txt out/
# run
java -cp out com.quizapp.Main
```

Requires JDK 17+.

## Questions data format
`src/main/resources/questions.txt`, pipe-separated:
```
category|type|text|opt1;opt2;opt3;opt4|correctAnswer
```
`type` is `MCQ` or `TF` (true/false leaves the options field empty).
Lines starting with `#` are comments. Add your own questions freely.

## Suggested Git history (one commit per feature)
```bash
git init
git add src/main/java/com/quizapp/model src/main/resources
git commit -m "add question models and data file"

git add src/main/java/com/quizapp/factory
git commit -m "add factory for question types"

git add src/main/java/com/quizapp/manager src/main/java/com/quizapp/session
git commit -m "add singleton score manager and session with observer"

git add src/main/java/com/quizapp/ui src/main/java/com/quizapp/Main.java
git commit -m "add quiz screens and timer logic"

git add src/test
git commit -m "add unit tests"
```
