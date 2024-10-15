import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

//** NOTE: This class doesn't actually need to exist, just copy paste the loading method to whatever class checks if a play is valid (probabbly board) */
public class Game {

    private ArrayList<Player> players;
    private Board board;
    private int currentPlayer;

    public Game()
    {
        players = new ArrayList<>();
        board = new Board();
        currentPlayer = 0;
    }

    /**
     * This method returns all of the players in the game.
     * @return An ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public boolean addPlayer()
    {
        players.add(new Player());
    }

    public Player findWinnner()
    {
        Player winner;
        int winnerScore = 0;
        for(Player player: players)
        {
            if(player.getScore() > winnerScore)
            {
                winner = player;
                winnerScore = player.getScore();
            }
        }

        return winner;
    }
}