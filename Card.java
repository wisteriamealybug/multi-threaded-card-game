public class Card {
    private int value;

    public Card(int value) throws IllegalArgumentException {
        if (value >= 0){
            this.value = value;
        } else{
            throw new IllegalArgumentException ("value must be non-negative");
        }
    }

    public int getValue(){
        return this.value;
    }
}