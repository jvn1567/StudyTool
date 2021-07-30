package studytool;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author John
 */
class MCQuestionSet {

    private Queue<MCQuestion> questionQueue;

    MCQuestionSet(String filename) {
        try {
            ArrayList<MCQuestion> questionSet = new ArrayList<>();
            Scanner read = new Scanner(new File(filename));
            while (read.hasNextLine()) {
                int choiceCount = Integer.parseInt(read.nextLine());
                String[] choices = new String[choiceCount];
                for (int i = 0; i < choiceCount; i++) {
                    choices[i] = read.nextLine();
                }
                questionSet.add(new MCQuestion(choices[0], choices));
            }
            shuffleQuestions(questionSet);
            for (MCQuestion question : questionSet) {
                questionQueue.add(question);
            }
        } catch (Exception ex) {
            //TEMP TODO FINISH PLEASE
        }
    }

    void shuffleQuestions(ArrayList<MCQuestion> questionSet) {
        for (int i = 0; i < questionSet.size(); i++) {
            Random rand = new Random();
            int swap = rand.nextInt() % questionSet.size();
            MCQuestion temp = questionSet.get(0);
            questionSet.set(0, questionSet.get(swap));
            questionSet.set(swap, temp);
        }
    }

    //TODO HANDLE NULL RETURNS ON EMPTY 
    MCQuestion keepFront() {
        return questionQueue.peek();
    }

    MCQuestion tossFront() {
        return questionQueue.poll();
    }
}
