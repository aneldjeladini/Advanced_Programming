import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface AnswerValidator {
    boolean validate(String userAnswer, String correctAnswer);
    void displayPrompt();
}

class TrueFalseValidator implements AnswerValidator {
    @Override
    public boolean validate(String userAnswer, String correctAnswer) {
        return !userAnswer.isEmpty() &&
                userAnswer.charAt(0) == correctAnswer.charAt(0);
    }

    @Override
    public void displayPrompt() {
        System.out.println("Enter 'T' for true or 'F' for false.");
    }
}

class FreeformValidator implements AnswerValidator {
    @Override
    public boolean validate(String userAnswer, String correctAnswer) {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    @Override
    public void displayPrompt() {
    }
}

class TriviaQuestion {
    private String question;
    private String answer;
    private int value;
    private AnswerValidator validator;

    public TriviaQuestion(String question, String answer, int value, AnswerValidator validator) {
        this.question = question;
        this.answer = answer;
        this.value = value;
        this.validator = validator;
    }

    public void display(int questionNumber) {
        System.out.println("Question " + questionNumber + ".  " + value + " points.");
        System.out.println(question);
        validator.displayPrompt();
    }

    public boolean checkAnswer(String userAnswer) {
        return validator.validate(userAnswer, answer);
    }

    public int getValue() {
        return value;
    }

    public String getCorrectAnswer() {
        return answer;
    }
}

class TriviaData {
    private List<TriviaQuestion> questions;

    public TriviaData() {
        questions = new ArrayList<>();
    }

    public void addQuestion(TriviaQuestion question) {
        questions.add(question);
    }

    public TriviaQuestion getQuestion(int index) {
        return questions.get(index);
    }

    public int getQuestionCount() {
        return questions.size();
    }
}

class GameController {
    private TriviaData triviaData;
    private int score;
    private Scanner scanner;

    public GameController() {
        triviaData = new TriviaData();
        score = 0;
        scanner = new Scanner(System.in);
        loadQuestions();
    }

    private void loadQuestions() {
        triviaData.addQuestion(new TriviaQuestion(
                "The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3, new FreeformValidator()));

        triviaData.addQuestion(new TriviaQuestion(
                "Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1, new TrueFalseValidator()));

        triviaData.addQuestion(new TriviaQuestion(
                "1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2, new FreeformValidator()));

        triviaData.addQuestion(new TriviaQuestion(
                "I am the geometric figure most like a lost parrot",
                "polygon", 2, new FreeformValidator()));

        triviaData.addQuestion(new TriviaQuestion(
                "Generics were introducted to Java starting at version 5.0.",
                "T", 1, new TrueFalseValidator()));
    }

    public void play() {
        for (int i = 0; i < triviaData.getQuestionCount(); i++) {
            askQuestion(i);
        }
        endGame();
    }

    private void askQuestion(int index) {
        TriviaQuestion question = triviaData.getQuestion(index);
        question.display(index + 1);

        String answer = scanner.nextLine();

        if (question.checkAnswer(answer)) {
            System.out.println("That is correct!  You get " + question.getValue() + " points.");
            score += question.getValue();
        } else {
            System.out.println("Wrong, the correct answer is " + question.getCorrectAnswer());
        }

        System.out.println("Your score is " + score);
    }

    private void endGame() {
        System.out.println("Game over!  Thanks for playing!");
    }
}

public class TriviaGame {
    public static void main(String[] args) {
        GameController game = new GameController();
        game.play();
    }
}
