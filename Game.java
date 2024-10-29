/**
 * The Game class contains the current state of Scrabble and the main line of execution.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/18/2024
 */

import java.io.File;
import java.util.*;

public class Game {

    private ArrayList<Player> players;
    private Board board;
    private int currentPlayer;
    private ArrayList<GameObserver> views;

    /**
     * Basic constructor for Game
     */
    public Game() {
        players = new ArrayList<>();
        board = new Board();
        currentPlayer = 0;
        views = new ArrayList<>();
    }

    /**
     * This method returns a desired player given their index
     *
     * @param index The index of the player to be extracted
     * @return The player located at the appropriate index
     */
    public Player getPlayer(int index) {
        return this.players.get(index);
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean addPlayer() {
        try {
            players.add(new Player());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Using the known player scores, determines the winner at the end of the game
     *
     * @return The Player who won
     */
    public Player findWinner() {
        Player winner = new Player();
        int winnerScore = 0;
        for (Player player : players) {
            if (player.getScore() > winnerScore) {
                winner = player;
                winnerScore = player.getScore();
            }
        }

        return winner;
    }

    /**
     * This method is responsible for adding a word to the board.
     * It extracts the letters and locations specified by the player.
     *
     * @param word - A dictionary that contains the letter and
     *             where it will be added to the board.
     * @return A boolean stating whether the word was successfully added to the board
     */
    public int addWord(Dictionary<ArrayList<Letter>, ArrayList<String>> word) {
        if (word.isEmpty()) {
            return -1;
        }

        ArrayList<Letter> letters = word.keys().nextElement(); //Extracting the letters
        ArrayList<String> locations = word.elements().nextElement(); //Extracting the locations

        return board.addWord(letters, locations);
    }

    /**
     * addView adds a GameObserver view to the list of views observing the game
     * @param view the view to be added
     */
    public void addView(GameObserver view)
    {
        this.views.add(view);
    }

    /**
     * removeView removes a view from the list of observers
     * @param view the view to be removed
     */
    public void removeView(GameObserver view)
    {
        views.remove(view);
    }

}