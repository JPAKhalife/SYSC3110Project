/**
 * The Letter class is responsible for tracking the state of scrabble
 * and contains the main line of execution
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/10/2024
 */

public class Letter {

    //Member variables
    private char letter;
    private int points;

    public Letter(char letter, char points) {
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
