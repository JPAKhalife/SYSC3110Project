/**
 * The Letter class is responsible for keeping track of which letters warrant which points.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/10/2024
 */

public class Letter {

    //Member variables
    private char letter;
    private int points;

    public Letter(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }
    public char getLetter() {
        return letter;
    }

    public int getPoints() {
        return points;
    }
}
