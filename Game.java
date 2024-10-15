import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

//** NOTE: This class doesn't actually need to exist, just copy paste the loading method to whatever class checks if a play is valid (probabbly board) */
public class Game {

    ArrayList<Player> players;

    public Game()
    {
        players = new ArrayList<>();
    }

    /**
     * This method returns all of the players in the game.
     * @return An ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }
}


