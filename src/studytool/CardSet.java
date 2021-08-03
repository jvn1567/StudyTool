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
class CardSet {

    private LinkedList<FlashCard> cardHand;

    CardSet(String filename) {
       try {
            ArrayList<FlashCard> cardSet = new ArrayList<>();
            Scanner read = new Scanner(new File(filename));
            while (read.hasNextLine()) {
                String front = read.nextLine();
                String back = read.nextLine();
                //catches separators and just in case there is no return at end
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
            System.out.println("FAILED LOADING CARDS");
        }
    }

    void shuffleCards(ArrayList<FlashCard> cardSet) {
        for (int i = 0; i < cardSet.size(); i++) {
            Random rand = new Random();
            int swap = rand.nextInt(cardSet.size());
            FlashCard temp = cardSet.get(0);
            cardSet.set(0, cardSet.get(swap));
            cardSet.set(swap, temp);
        }
    }

    //TODO HANDLE NULL RETURNS ON EMPTY
    FlashCard peekFront() {
        return cardHand.peek();
    }
    
    FlashCard keepFront() {
        FlashCard temp = cardHand.poll();
        cardHand.add(temp);
        return temp;
    }

    FlashCard tossFront() {
        return cardHand.poll();
    }
    
    int getSize() {
        return cardHand.size();
    }
}
