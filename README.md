# Simple QuizApp
This is a simplified version of a Quiz application intended for a university SCD lab. It follows an Eclipse-style Java project layout and keeps the code intentionally small and easy to read.

Features (fulfilled requirements):
- Simple Eclipse-style project (no Maven required).
- Only two main classes: `QuizGUI.java` (GUI) and `QuizLogic.java` (logic).
- Clear separation between GUI and logic.
- Uses basic Swing components (`JFrame`, `JPanel`, `JButton`, `JLabel`, `JRadioButton`).
- Home → Category selection → Timed Quiz → Results screen flow.
- Beginner-friendly code with clear variable names and inline comments.
- Simple exception handling using `try-catch` and `IllegalArgumentException` with messages shown via `JOptionPane`.
- A few JUnit 4 test cases in `test/QuizLogicTest.java`.

How to open and run in Eclipse
1. Import the project as an existing Java project or create a new Java project and copy the `src/` and `test/` folders into the project root.
2. Make sure the JRE System Library is added to the project's build path (Java 8+).
3. Run `src/QuizGUI.java` as a Java Application (this launches the GUI).

Running tests
- The tests use JUnit 4. If your Eclipse workspace does not have JUnit 4 on the classpath, add it via Project > Properties > Java Build Path > Libraries.
- Run `test/QuizLogicTest.java` as a JUnit test.

Files
- `src/QuizGUI.java` — main GUI and flow handling.
- `src/QuizLogic.java` — quiz data structures and scoring logic.
- `src/questions.txt` — sample questions loaded at runtime (simple text format).
- `test/QuizLogicTest.java` — basic JUnit4 tests.

Questions file format
Each non-empty line uses this pipe-separated format:
```
category|question|opt1;opt2;opt3;opt4|answerIndex|timeSeconds
```
Lines starting with `#` are ignored as comments.

Screenshots
- (Add screenshots here if you like: Home screen, Category selection, Quiz, Results.)

Notes
- The goal of this rewrite is simplicity and clarity for students learning SCD. The project intentionally avoids advanced patterns and build systems.