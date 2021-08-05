package studytool;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author John
 */
class MCQuestionSet {

    private LinkedList<MCQuestion> questionQueue;

    MCQuestionSet(String filename) {
        try {
            ArrayList<MCQuestion> questionSet = new ArrayList<>();
            Scanner read = new Scanner(new File(filename));
            while (read.hasNextLine()) {
                int choiceCount = Integer.parseInt(read.nextLine());
                String question = read.nextLine();
                String[] choices = new String[choiceCount];
                for (int i = 0; i < choiceCount; i++) {
                    choices[i] = read.nextLine();
                }
                //catches separators and disregards missing return at end
                if (read.hasNextLine()) {
                    read.nextLine();
                }
                questionSet.add(new MCQuestion(question, choices));
            }
            shuffleQuestions(questionSet);
            questionQueue = new LinkedList<>();
            for (MCQuestion question : questionSet) {
                questionQueue.add(question);
            }
        } catch (Exception ex) {
            System.out.println("FAILED LOADING QUESTIONS");
        }
    }

    void shuffleQuestions(ArrayList<MCQuestion> questionSet) {
        for (int i = 0; i < questionSet.size(); i++) {
            Random rand = new Random();
            int swap = rand.nextInt(questionSet.size());
            MCQuestion temp = questionSet.get(0);
            questionSet.set(0, questionSet.get(swap));
            questionSet.set(swap, temp);
        }
    }

    MCQuestion peekNext() {
        return questionQueue.peek();
    }
    
    MCQuestion getNext() {
        return questionQueue.poll();
    }
    
    int getSize() {
        return questionQueue.size();
    }
}
