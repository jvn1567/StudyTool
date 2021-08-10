package studytool;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents a set of quiz scores for a user. Each quiz score will
 * have the name of the quiz, along with the score earned and maximum score.
 *
 * @author John Nguyen (https://github.com/jvn1567)
 */
class QuizScores {

    private ArrayList<String> quizName;
    private ArrayList<Integer> score;
    private ArrayList<Integer> maxScore;

    /**
     * Constructs an empty QuizScores object.
     */
    QuizScores() {
        quizName = new ArrayList<>();
        score = new ArrayList<>();
        maxScore = new ArrayList<>();
    }

    /**
     * Constructs a QuizScores object with scores loaded from the passed-in
     * file.
     *
     * @param filename a String with the file path to the .txt file that stores
     * user quiz scores
     */
    QuizScores(String filename) {
        this();
        try {
            Scanner read = new Scanner(new File(filename));
            while (read.hasNextLine()) {
                String line = read.nextLine();
                int left = line.lastIndexOf("(");
                int mid = line.lastIndexOf("/");
                int right = line.lastIndexOf(")");
                int score = Integer.parseInt(line.substring(left + 1, mid));
                int max = Integer.parseInt(line.substring(mid + 1, right));
                add(line.substring(0, left - 2), score, max);
            }
        } catch (Exception ex) {
            //no scores file found or invalid format, start with no scores
        }

    }

    /**
     * Adds a quiz score with the passed in quiz name, score, and max score only
     * if the quiz name is currently not already stored, or the score for the
     * passed in quiz is higher than the one stored.
     *
     * @param quiz the name of the quiz
     * @param score the score earned
     * @param max the maximum score
     */
    void add(String quiz, int score, int max) {
        int spot = find(quiz);
        //new quiz push back
        if (getSize() == spot) {
            quizName.add(quiz);
            this.score.add(score);
            maxScore.add(max);
            //new quiz insertion
        } else if (spot < 0 || (spot == 0 && !quizName.get(spot).equals(quiz))) {
            quizName.add(-spot, quiz);
            this.score.add(-spot, score);
            maxScore.add(-spot, max);
            //score for quiz exists
        } else if (quizName.get(spot).equals(quiz) && this.score.get(spot) < score) {
            this.score.set(spot, score);
            maxScore.set(spot, max);
        }
    }

    /**
     * Binary search for the passed in quiz name.
     *
     * @param quiz the quiz name
     * @param min the left search area bound
     * @param max the right search area bound
     * @return an integer representing the location found, or a negative integer
     * whose absolute value is the location to insert the quiz and maintain
     * alphabetical ordering.
     */
    private int find(String quiz, int min, int max) {
        int mid = (min + max) / 2;
        //base cases
        if (quizName.get(mid).equalsIgnoreCase(quiz)) {
            return mid;
        } else if (mid == max && quizName.get(mid).compareToIgnoreCase(quiz) > 0) {
            return -(mid);
        } else if (mid == max && quizName.get(mid).compareToIgnoreCase(quiz) < 0) {
            return -(mid + 1);
            //search left half
        } else if (quizName.get(mid).compareToIgnoreCase(quiz) > 0) {
            return find(quiz, min, mid - 1);
            //search right half
        } else {
            return find(quiz, mid + 1, max);
        }
    }

    /**
     * Finds the index of the passed in quiz name, or where it should be
     * inserted to maintain alphabetical order. If there are no quizzes
     * currently, returns 0 to insert at the start.
     *
     * @param quiz the name of the quiz to find
     * @return the index to place the quiz
     */
    private int find(String quiz) {
        if (!quizName.isEmpty()) {
            return find(quiz, 0, quizName.size() - 1);
        } else {
            return 0;
        }
    }

    /**
     * Returns the number of quizzes stored.
     *
     * @return number of quizzes stored
     */
    int getSize() {
        return quizName.size();
    }

    /**
     * Returns the quiz score at the passed in index as a String, with the
     * format: "Quiz Name: (score/max)".
     *
     * @param index the index of the score to retrieve
     * @return a String with the full score
     */
    String getScore(int index) {
        if (index >= 0 && index < getSize()) {
            return quizName.get(index) + ": (" + score.get(index) + "/"
                    + maxScore.get(index) + ")";
        } else {
            return "";
        }
    }

    /**
     * Returns a string with all scores printed, with each entry in the format"
     * "Quiz Name: (score/max)" and a return character separating each entry.
     *
     * @return a String with all quiz score entries separated by returns
     */
    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < quizName.size(); i++) {
            output += (getScore(i) + "\n");
        }
        return output;
    }

}
