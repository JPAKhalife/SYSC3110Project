import java.io.File;
import java.util.*;

//** NOTE: This class doesn't actually need to exist, just copy paste the loading method to whatever class checks if a play is valid (probabbly board) */
public class Game {

    private ArrayList<Player> players;
    private Board board;
    private int currentPlayer;

    /**
     * Basic constructor for Game
     */
    public Game()
    {
        players = new ArrayList<>();
        board = new Board();
        currentPlayer = 0;
    }

    /**
     * This method returns a desired player given their index
     * @param index The index of the player to be extracted
     * @return The player located at the appropriate index
     */
    public Player getPlayer(int index) {
        return this.players.get(index);
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

    /**
     * Using the known player scores, determines the winner at the end of the game
     * @return The Player who won
     */
    public Player findWinner()
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

    public boolean placeWord(Dictionary<ArrayList<Letter>, ArrayList<String>> word)
    {
        ArrayList<Letter> letters = word.keys().nextElement(); //Extracting the letters
        ArrayList<String> locations = word.elements().nextElement(); //Extracting the locatiosn

        return false; //NEED to add board to input
    }

    public static void main(String[] args) {
        Game game = new Game();

        for (int i = 0; i < 4; i++) {
            game.addPlayer();
        }

        Player currentPlayer = game.getPlayer(0);

        while(currentPlayer.pullFromBag())
        {
            Dictionary<ArrayList<Letter>, ArrayList<String>> word =  currentPlayer.playerTurn();
            boolean success = Game.addWord(word);

            if(success)
            {
                
            }
        }




    }
}