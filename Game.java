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

        int wordScore = board.addWord(letters, locations);
        if (wordScore < 0) {
            for (int i = 0 ; i < views.size() ; i++) {
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

    public static void main(String[] args) {
        LetterBag.createBag();
        Game game = new Game();
        boolean success = false;
        boolean gameOn = true;
        int playerIndex = 0;
        Display gui = new Display(game);
        Scanner scan = new Scanner(System.in);
        int numPlayers = 0;

        //Adding the number of players the user wants to the game
        while (!success) {
            System.out.print("Enter the number of players that will be playing (2 - 4): ");
            numPlayers = scan.nextInt();

            if ((numPlayers < 5) && (numPlayers > 1)) {
                success = true;
                scan.nextLine(); //clearing buffer
            }
        }

        //Adding a standard 4 players
        for (int i = 0; i < numPlayers; i++) {
            game.addPlayer();
            boolean working = game.getPlayer(i).pullFromBag();

            if (!working) {
                System.out.println("Failed to pull from bag");
            }

        }

        //Starting player
        Player currentPlayer = game.getPlayer(playerIndex);

        //While there are still letters to pull from the bag, and no player's rack is empty, the game continues
        while (gameOn) {
            int turnPoints = 0;
            success = false;

            //Displaying the board for the players
            gui.displayBoard();

            //Player can attempt over and over again to create a proper word
            while (!success) {
                System.out.println("It is player " + (playerIndex + 1) + "'s turn.");
                Dictionary<ArrayList<Letter>, ArrayList<String>> word = currentPlayer.playerTurn();
                turnPoints = game.addWord(word);
                success = (turnPoints != 0);

                if (!success) {
                    System.out.println("Please try again.\n");
                }
            }

            //Ensuring the player can never lose points
            if (turnPoints < 0) {
                turnPoints = 0;
            }

            //Only update the score if the user's word is valid
            currentPlayer.updateScore(turnPoints);

            //player pulls from the bag until they have 7 letters in their rack
            boolean bagNotEmpty = currentPlayer.pullFromBag();
            //if the user's rack is empty, the game is over
            if (currentPlayer.isRackEmpty() && !bagNotEmpty) {
                gameOn = false;
            }

            //displaying the player's updated score to them
            gui.showScores();

            //updating which player is working
            playerIndex = (playerIndex + 1) % numPlayers;

            currentPlayer = game.getPlayer(playerIndex);
        }

        //Game is over, now must calculate the winner
        Player winner = game.findWinner();

    }
}