package studytool;

import java.util.Random;

/**
 *
 * @author John
 */
class MCQuestion {
    private String question;
    private String answer;
    private String[] choices;
    
    MCQuestion(String question, String[] choices) {
        this.question = question;
        this.answer = choices[0];
        this.choices = choices;
        shuffleChoices();
    }
    
    String getQuestion() {
        return question;
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
            int swap = rand.nextInt(choices.length);
            String temp = choices[0];
            choices[0] = choices[swap];
            choices[swap] = temp;
        }
    }
    
    boolean isCorrect(String choice) {
        return choice.equals(answer);
    }
}
