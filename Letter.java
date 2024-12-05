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

    /**
     * Overridden equals method - needed for the blank tile
     * @param obj - the object to compare with.
     * @return whether or not the Letters are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Letter l = (Letter) obj;
        return (l.getPoints() == this.getPoints()) && (l.getLetter() == this.getLetter());
    }

    /**
     * Overridden hash code method
     * This is needed due to the blank tile
     * @return
     */
    @Override
    public int hashCode() {
        return Character.hashCode(letter);
    }

    /**
     * This method is used to set the character of a letter
     * @param letter - the letter that will be changed to
     */
    public void setLetter(char letter) {
        this.letter = letter;
    }

}