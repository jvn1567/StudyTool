package studytool;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represents a set of multiple choice questions in a Quiz. The
 * questions are stored as a queue of MCQuestions in a LinkedList.
 *
 * @author John Nguyen (https://github.com/jvn1567)
 */
class MCQuestionSet {

    private LinkedList<MCQuestion> questionQueue;

    /**
     * Constructs a set of MCQuestion objects from the passed-in file. Questions
     * are then shuffled and added to the queue.
     *
     * @param filename the file path to the text file with question data
     */
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
            //failed loading, bad file type or invalid format
        }
    }

    /**
     * Shuffles questions in the passed-in array.
     *
     * @param questionSet the array to shuffle
     */
    void shuffleQuestions(ArrayList<MCQuestion> questionSet) {
        for (int i = 0; i < questionSet.size(); i++) {
            Random rand = new Random();
            int swap = rand.nextInt(questionSet.size());
            MCQuestion temp = questionSet.get(0);
            questionSet.set(0, questionSet.get(swap));
            questionSet.set(swap, temp);
        }
    }

    /**
     * Returns the MCQuestion currently at the front of the queue.
     *
     * @return the front question
     */
    MCQuestion peekNext() {
        return questionQueue.peek();
    }

    /**
     * Returns the MCQuestion currently at the front of the queue and removes it
     * from the queue.
     *
     * @return the question removed from the queue
     */
    MCQuestion getNext() {
        return questionQueue.poll();
    }

    /**
     * Returns the number of questions in the queue.
     *
     * @return the number of MCQuestions
     */
    int getSize() {
        return questionQueue.size();
    }
}
