package studytool;

/**
 * This class represents a single flashcard with text on its front and back
 * sides.
 *
 * @author John Nguyen (https://github.com/jvn1567)
 */
class FlashCard {

    private String front;
    private String back;

    /**
     * Constructs a single flashcard with the passed in front and back texts.
     *
     * @param front the text on the front of the card
     * @param back the text on the back of the card
     */
    FlashCard(String front, String back) {
        this.front = front;
        this.back = back;
    }

    /**
     * Returns a String with the text from the front of the card.
     *
     * @return the front text
     */
    String getFront() {
        return front;
    }

    /**
     * Returns a String with the text from the back of the card.
     *
     * @return the back text
     */
    String getBack() {
        return back;
    }

    /**
     * Returns a String with the whole card's text, with the front and back
     * texts separated by a return character.
     *
     * @return a string with the card text
     */
    @Override
    public String toString() {
        return front + "\n" + back;
    }

}
