package studytool;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represents a set of FlashCards used for subject review. It stores
 * a queue of FlashCard objects in a LinkedList.
 *
 * @author John Nguyen (https://github.com/jvn1567)
 */
class CardSet {

    private LinkedList<FlashCard> cardHand;

    /**
     * Constructs a CardSet object with FlashCard data read from the passed-in
     * file. Individual FlashCards are shuffled and added to a queue to review.
     *
     * @param filename the file path to the card set's text file
     */
    CardSet(String filename) {
        try {
            ArrayList<FlashCard> cardSet = new ArrayList<>();
            Scanner read = new Scanner(new File(filename));
            while (read.hasNextLine()) {
                String front = read.nextLine();
                String back = read.nextLine();
                //catches separators and disregards missing return at end
                if (read.hasNextLine()) {
                    read.nextLine();
                }
                FlashCard card = new FlashCard(front, back);
                cardSet.add(card);
            }
            shuffleCards(cardSet);
            cardHand = new LinkedList<>();
            for (int i = 0; i < cardSet.size(); i++) {
                cardHand.add(cardSet.get(i));
            }
        } catch (Exception ex) {
            //cards failed to load, invalid file type or format
        }
    }

    /**
     * Shuffles the cards in the passed in array of cards.
     *
     * @param cardSet the array to shuffle
     */
    void shuffleCards(ArrayList<FlashCard> cardSet) {
        for (int i = 0; i < cardSet.size(); i++) {
            Random rand = new Random();
            int swap = rand.nextInt(cardSet.size());
            FlashCard temp = cardSet.get(0);
            cardSet.set(0, cardSet.get(swap));
            cardSet.set(swap, temp);
        }
    }

    /**
     * Returns the FlashCard at the front of the queue
     *
     * @return the front FlashCard
     */
    FlashCard peekFront() {
        return cardHand.peek();
    }

    /**
     * Returns the FlashCard at the front of the queue and moves it from the
     * front of the queue to the back.
     *
     * @return the card that was moved from the front to back of the queue
     */
    FlashCard keepFront() {
        FlashCard temp = cardHand.poll();
        cardHand.add(temp);
        return temp;
    }

    /**
     * Returns the FlashCard at the front of the queue and removes it from the
     * front of the queue.
     *
     * @return the card that was removed from the front of the queue
     */
    FlashCard tossFront() {
        return cardHand.poll();
    }

    /**
     * Returns the number of FlashCards in this set.
     *
     * @return the number of FlashCards
     */
    int getSize() {
        return cardHand.size();
    }
}
