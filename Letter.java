import java.io.Serializable;

/**
 * The Letter class is responsible for keeping track of letters and their value in points.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/10/2024
 */

public class Letter implements Serializable {

    //Member variables
    private char letter;
    private int points;

    public Letter(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }

    /**
     * An accessor method that gets the letter character stored by the object
     * @return the character contained by the object
     */
    public char getLetter() {
        return letter;
    }

    /**
     * A getPoints method that gets the number of points that a letter is worth.
     * @return an integer representing a number of points.
     */
    public int getPoints() {
        return points;
    }
}