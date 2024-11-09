/**
 * The Game class contains the current state of Scrabble and the main line of execution.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/18/2024
 */

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class Game {

    private ArrayList<Player> players;
    private Board board;
    private int currentPlayer;
    private ArrayList<GameObserver> views;


    /**
     * Basic constructor for Game
     */
    public Game(int playerNum) {
        players = new ArrayList<>();
        board = new Board();
        currentPlayer = 0;
        views = new ArrayList<>();
        LetterBag.createBag();

        for(int i = 0; i < playerNum; i++)
        {
            addPlayer();
        }
    }

    /**
     * This method returns a desired player given their index
     *
     * @return The player located at the appropriate index
     */
    public Player getCurrentPlayer() {
        return this.players.get(currentPlayer);
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
     * Using the known player scores, determines the player with the highest score at the moment
     * Intended to be used at the end of the game to find the winner
     *
     * @return The Player who won
     */
    public int findWinner() {
        int winner = -1;
        int winnerScore = 0;
        for (int i = 0; i< players.size(); i++) {
            if (players.get(i).getScore() > winnerScore) {
                winner = i;
                winnerScore = players.get(i).getScore();
            }
        }

        //Tell the view to update the winner
        for(GameObserver view: views)
        {
            view.handleScoreUpdate(winner);
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
            System.out.println("Error: never added word to the board\n");
            return -1;
        }

        ArrayList<Letter> letters = word.keys().nextElement(); //Extracting the letters
        ArrayList<String> locations = word.elements().nextElement(); //Extracting the locations

        int wordScore = board.addWord(letters, locations);
        System.out.println("Added word to the board. Score: "+wordScore+"\n");

        if (wordScore > 0) {
            for (int i = 0 ; i < views.size() ; i++) {
                System.out.println("Handling the board update\n");
                views.get(i).handleBoardUpdate(getBoard().getStatus());
            }
        }
        return wordScore;
    }

    /**
     * addView adds a GameObserver view to the list of views observing the game
     *
     * @param view the view to be added
     */
    public void addView(GameObserver view) {
        this.views.add(view);
    }

    /**
     * removeView removes a view from the list of observers
     *
     * @param view the view to be removed
     */
    public void removeView(GameObserver view) {
        views.remove(view);
    }

    /**
     * handleNewTurn performs the necessary actions to change which player's turn it is
     */
    public void handleNewTurn()
    {

        //Giving the next player a turn
        currentPlayer = (currentPlayer + 1) % players.size();

        //displaying the updated scores and board statuses
        for(GameObserver view: views)
        {
            view.handleScoreUpdate(-1);
            view.handleNewTurn(currentPlayer);
        }

        //Other things that need to be done somewhere:
            //2. Checking if the game is over via the bag being empty
            //3. If so, finding the winner
            //4. Otherwise, call this function to make the next turn occur

    }

    /**
     * This method is called when the Board returns an error, and updates all the
     * views accordingly
     */
    public void handleBoardError()
    {
        for(GameObserver view: views)
        {
            view.handleBoardUpdate(board.getStatus());
        }
    }

    public ArrayList<GameObserver> getViews(){
        return this.views;
    }
}