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

    public ArrayList<Player> getPlayers(){ return new ArrayList<Player>(players);}

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

    public boolean addWord(Dictionary<ArrayList<Letter>, ArrayList<String>> word)
    {
        ArrayList<Letter> letters = word.keys().nextElement(); //Extracting the letters
        ArrayList<String> locations = word.elements().nextElement(); //Extracting the locations

        return board.addWord(letters, locations);
    }

    public static void main(String[] args) {
        boolean playerRackEmpty = false; //Useful for determining whether the game should finish or not

        Game game = new Game();

        //Adding a standard 4 players
        for (int i = 0; i < 4; i++) {
            game.addPlayer();
        }

        //Starting player
        Player currentPlayer = game.getPlayer(0);

        //While there are still letters to pull from the bag, and no player's rack is empty, the game continues
        while(currentPlayer.pullFromBag())
        {
            Dictionary<ArrayList<Letter>, ArrayList<String>> word =  currentPlayer.playerTurn();
            boolean success = game.addWord(word);

            if(success)
            {
                
            }
        }




    }
}