/**
 * The Display class is responsible for displaying information about the game.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/10/2024
 */
public class Display {

    //Member variables
    Game game;

    //Constructor
    public Display(Game game) {
        this.game = game;
    }

    /**
     * This method is intended to print the current state of the board.
     */
    public void displayBoard() {
        System.out.println("Current board: ");
        System.out.println(game.getBoard().toString());
    }

    /**
     * This method is intended to reveal the score of all players
     */
    public void showScores() {
        System.out.println("Player scores:");
        for (int i = 0 ; i < this.game.getPlayers().size() ; i++) {
            System.out.println("Player " + (i + 1) + ": " + this.game.getPlayers().get(i).getScore());
        }
    }

    /**
     * This method shows the score of a particular player.
     * @param playerNum - the player whose score will be displayed.
     */
    public void showScore(int playerNum) {
        //This method should get the name of the player or the playe number
       // System.out.println("Player " + playerNum + ", your score is: " + this.game.getPlayer(playerNum).getScore());
    }
}