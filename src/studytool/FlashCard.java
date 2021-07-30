package studytool;

/**
 *
 * @author John
 */
class FlashCard {

    private String front;
    private String back;

    FlashCard(String front, String back) {
        this.front = front;
        this.back = back;
    }
    
    String getFront() {
        return front;
    }
    
    String getBack() {
        return back;
    }
}
