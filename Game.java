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
        try
        {
            players.add(new Player());
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public Player findWinnner()
    {
        Player winner = new Player();
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

    public static void main(String[] args) {
        Game game = new Game();

        for (int i = 0; i < 4; i++) {
            game.addPlayer();
        }

    }
}