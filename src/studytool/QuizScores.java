/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studytool;

import java.util.ArrayList;

/**
 *
 * @author John Nguyen
 */
class QuizScores {
    
    private ArrayList<String> quizName;
    private ArrayList<Integer> score;
    private ArrayList<Integer> maxScore;
    
    QuizScores() {
        quizName = new ArrayList<>();
        score = new ArrayList<>();
        maxScore = new ArrayList<>();
    }
    
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
    
    private int find(String quiz) {
        if (!quizName.isEmpty()) {
            int temp = find(quiz, 0, quizName.size() - 1);
            System.out.println(temp);
            return temp;
        } else {
            return 0;
        }
    }
    
    int getSize() {
        return quizName.size();
    }
    
    String getScore(int index) {
        if (index >= 0 && index < getSize()) {
            return quizName.get(index) + ": (" + score.get(index) + "/" +
                    maxScore.get(index) + ")";
        } else {
            return "";
        }
    }
    
    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < quizName.size(); i++) {
            output += getScore(i);
            output += maxScore.get(i) + "\n";
        }
        return output;
    }
    
}
