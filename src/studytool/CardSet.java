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
class CardSet {

    private Queue<FlashCard> cardHand;

    CardSet(String filename) {
        try {
            ArrayList<FlashCard> cardSet = new ArrayList<>();
            Scanner read = new Scanner(new File(filename));
            while (read.hasNextLine()) {
                String front = read.nextLine();
                String back = read.nextLine();
                FlashCard card = new FlashCard(front, back);
                cardSet.add(card);
            }
            shuffleCards(cardSet);
            for (FlashCard card : cardSet) {
                cardHand.add(card);
            }
        } catch (Exception ex) {
            //TEMP TODO FINISH PLEASE
        }
    }

    void shuffleCards(ArrayList<FlashCard> cardSet) {
        for (int i = 0; i < cardSet.size(); i++) {
            Random rand = new Random();
            int swap = rand.nextInt() % cardSet.size();
            FlashCard temp = cardSet.get(0);
            cardSet.set(0, cardSet.get(swap));
            cardSet.set(swap, temp);
        }
    }

    //TODO HANDLE NULL RETURNS ON EMPTY 
    FlashCard keepFront() {
        return cardHand.peek();
    }

    FlashCard tossFront() {
        return cardHand.poll();
    }
}
