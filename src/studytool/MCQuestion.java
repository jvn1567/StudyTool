package studytool;

import java.util.Random;

/**
 * This class represents a single multiple choice quiz question.
 *
 * @author John Nguyen (https://github.com/jvn1567)
 */
class MCQuestion {

    private String question;
    private String answer;
    private String[] choices;

    /**
     * Constructs a single multiple choice question. It is assumed that the
     * first answer in the passed-in array of possible choices is the correct
     * one.
     *
     * @param question the question that needs to be answered
     * @param choices the set of possible answer choices.
     */
    MCQuestion(String question, String[] choices) {
        this.question = question;
        this.answer = choices[0];
        this.choices = choices;
        shuffleChoices();
    }

    /**
     * Returns the question being asked.
     *
     * @return the question as a String
     */
    String getQuestion() {
        return question;
    }

    /**
     * Returns the correct answer to the question.
     *
     * @return the answer as a String
     */
    String getAnswer() {
        return answer;
    }

    /**
     * Returns the array of possible answers to the question.
     *
     * @return a String[] with the possible answer choices
     */
    String[] getChoices() {
        return choices;
    }

    /**
     * Shuffles the answer choices for the question.
     */
    void shuffleChoices() {
        for (int i = 0; i < choices.length; i++) {
            Random rand = new Random();
            int swap = rand.nextInt(choices.length);
            String temp = choices[0];
            choices[0] = choices[swap];
            choices[swap] = temp;
        }
    }

    /**
     * Returns whether the passed-in answer choice matches the correct answer.
     *
     * @param choice the answer choice to check
     * @return true if the answer is the correct answer, false if incorrect
     */
    boolean isCorrect(String choice) {
        return choice.equals(answer);
    }
}
