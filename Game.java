/**
 * The Game class contains the current state of Scrabble and the main line of execution.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/10/2024
 */

import java.io.File;
import java.util.*;

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

    /**
     * This method is responsible for adding a word to the board. 
     * It extracts the letters and locations specified by the player.
     * @param word - A dictionnary that contains the letter and 
     * where it will be added to the board.
     * @return A boolean stating whether the word was successfully added to the board
     */
    public boolean addWord(Dictionary<ArrayList<Letter>, ArrayList<String>> word)
    {
        ArrayList<Letter> letters = word.keys().nextElement(); //Extracting the letters
        ArrayList<String> locations = word.elements().nextElement(); //Extracting the locations

        return board.addWord(letters, locations);
    }

    public static void main(String[] args) {

        Game game = new Game();
        boolean success = false;
        boolean gameOn = true;
        int playerIndex = 0;

        //Adding a standard 4 players
        for (int i = 0; i < 4; i++) {
            game.addPlayer();
        }

        //Starting player
        Player currentPlayer = game.getPlayer(playerIndex);

        //While there are still letters to pull from the bag, and no player's rack is empty, the game continues
        while(gameOn)
        {
            //Player can attempt over and over again to create a proper word
            while(!success) {
                Dictionary<ArrayList<Letter>, ArrayList<String>> word = currentPlayer.playerTurn();
                success = game.addWord(word);
            }
            //Only update the score if the user's word is valid
            currentPlayer.updateScore();

            //player pulls from the bag until they have 7 letters in their rack
            boolean bagNotEmpty = currentPlayer.pullFromBag();
            //if the user's rack is empty, the game is over
            if(currentPlayer.isRackEmpty() && !bagNotEmpty)
            {
                gameOn = false;
            }

            currentPlayer = game.getPlayer(playerIndex);
        }

    }
}