package studytool;

import java.util.Random;

/**
 *
 * @author John
 */
class MCQuestion {
    private String answer;
    private String[] choices;
    
    MCQuestion(String answer, String[] choices) {
        this.answer = answer;
        this.choices = choices;
        shuffleChoices();
    }
    
    String getAnswer() {
        return answer;
    }
    
    String[] getChoices() {
        return choices;
    }
    
    void shuffleChoices() {
        for (int i = 0; i < choices.length; i++) {
            Random rand = new Random();
            int swap = rand.nextInt() % choices.length;
            String temp = choices[0];
            choices[0] = choices[swap];
            choices[swap] = temp;
        }
    }
    
    boolean isCorrect(String choice) {
        return choice.equals(answer);
    }
}
