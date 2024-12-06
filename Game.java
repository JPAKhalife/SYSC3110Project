/**
 * The Game class contains the current state of Scrabble and the main line of execution.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani
 * @date 08/18/2024
 */

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Player> players;
    private Board board;
    private int currentPlayer;
    private ArrayList<GameObserver> views;
    private static final int TIMER_DELAY_S = 5;
    private boolean doTimer;
    private boolean waitingForInput;
    private int timerValue;
    private Timer timer;


    /**
     * Basic constructor for Game
     */
    public Game(int playerNum, int AIplayerNum, String filename) {
        players = new ArrayList<>();
        board = new Board(filename);
        currentPlayer = 0;
        waitingForInput = false;
        views = new ArrayList<>();
        timer = new Timer();
        timerValue = TIMER_DELAY_S;
        doTimer = false;
        LetterBag.createBag();

        for(int i = 0; i < playerNum; i++)
        {
            addPlayer();
        }

        for(int i = 0; i< AIplayerNum; i++)
        {
            addAIplayer();
        }
    }

    /**
     * This method returns a desired player given their index
     *
     * @return The player located at the appropriate index
     */
    public Player getCurrentPlayer() {
        if(currentPlayer < this.players.size()){
            return this.players.get(currentPlayer);
        }else{
            return this.players.get(currentPlayer - this.players.size());
        }
    }

    public ArrayList<Player> getPlayers() {
        //create new list of players to hold all players in game (including AI)
        ArrayList<Player> allPlayers = new ArrayList<Player>(players);

        return allPlayers;
    }

    public Board getBoard() {
        return this.board;
    }

    protected boolean addPlayer() {
        try {
            players.add(new Player());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean addAIplayer(){
        try{
            players.add(new AIPlayer(board));
            return true;
        } catch (Exception e){
            return false;
        }
    }
    /**
     * Using the known player scores, determines the player with the highest score at the moment
     * Intended to be used at the end of the game to find the winner
     *
     * @return The Player who won
     */
    protected int findWinner() {
        int winner = -1;
        int winnerScore = 0;

        for (int i = 0; i < players.size(); i++) {
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
            return -1;
        }

        ArrayList<Letter> letters = word.keys().nextElement(); //Extracting the letters
        ArrayList<String> locations = word.elements().nextElement(); //Extracting the locations

        int wordScore = board.addWord(letters, locations);

        handleBoardError();
        return wordScore;
    }

    public void saveGame(String filename) {
        try{
            FileOutputStream out = new FileOutputStream(filename);
            ObjectOutputStream obj = new ObjectOutputStream(out);
            obj.writeObject(this);
            obj.close();
            out.close();
            System.out.println("Game saved successfully to " + filename);
        }
        catch(Exception e){
            System.out.println("An error occured while saving game");
            e.printStackTrace();

        }

    }

    /**
     * loadGame deserializes the game that is saved in a file and loads it
     * @param filename the filename to be imported from
     */
    public void loadGame(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File not found");
                return;
            }

            ObjectInputStream inObj = new ObjectInputStream(new FileInputStream(filename));
            Game loadedGame = (Game) inObj.readObject();
            inObj.close();

            if (loadedGame == null || loadedGame.getPlayers().isEmpty() || loadedGame.getBoard() == null) {
                System.out.println("Game cannot be loaded");
                return;

            }
            this.board = loadedGame.board;
            this.players = loadedGame.players;
            this.currentPlayer = loadedGame.currentPlayer;


            for(GameObserver view: views){
                view.handleBoardUpdate(board.getStatus());
                view.handleNewTurn(currentPlayer);
                view.handleScoreUpdate(-1);
            }

            System.out.println("Game loaded successfully");

        } catch (IOException e) {
            System.out.println("An error occured while loading game");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Game is not a valid game class");
            e.printStackTrace();
        }
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
        setInputWait(true);
        //clearing the stacks
        getCurrentPlayer().clearCollections();
        //Giving the next player a turn (including AI players)
        //turn order priority favours real players. Once all real players have finished, the AI players will play
        currentPlayer = (currentPlayer + 1) % (players.size());

        while(this.players.get(currentPlayer) instanceof AIPlayer){
            AIPlayer currentAI = (AIPlayer) this.players.get(currentPlayer);
            currentAI.aiTurn();
            currentPlayer = (currentPlayer + 1) % (players.size()); //incrementing again, since the AI just went
        }
        System.out.println("chandlealled");

        if (this.doTimer) {
            setTimerValue(TIMER_DELAY_S);
            for (GameObserver view : views) {
                view.handleTimerUpdate(getTimerValue(),this.doTimer);
            }
        }

        //Clear the board status
        board.clearStatus();
        //displaying the updated scores and board statuses
        for(GameObserver view: views)
        {
            view.handleBoardUpdate(board.getStatus());
            view.handleScoreUpdate(-1);
            view.handleNewTurn(currentPlayer);
        }

        LetterBag.printContents();
        setInputWait(false);
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
        board.clearStatus();
    }

    /**
     * Returns the current observers of this instance of Game
     * @return An ArrayList of all the objects currently observing the Game
     */
    public ArrayList<GameObserver> getViews(){
        return this.views;
    }

    /**
     * This method turns on and off the timer.
     */
    public void toggleTimer() {
        this.doTimer  = !this.doTimer;
        if (this.doTimer) {
            activateTimer();
        } else {
            this.timer.cancel();
            for (GameObserver view : views) {
                view.handleTimerUpdate(this.timerValue,this.doTimer);
            }
        }
    }

    /**
     * This method activates the timer
     */
    public void activateTimer() {
        this.timer = new Timer(); //Reinitialize the timer to start a new thread
        //Declare the 1 second periodic task
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (GameObserver view : views) {
                    if (getTimerValue() > 0) {
                        System.out.println("ddecrememnt");
                        setTimerValue(getTimerValue() - 1); //update the timer and display
                        view.handleTimerUpdate(timerValue,doTimer);
                    }
                    if (getTimerValue() <= 0) {
                        //If a JOptionPane is open when the turn increments:
                        //the submit button has already been pressed
                        //Wait for that part of the thread to complete.
                        if (isWaitingForInput()) {
                            //Stop the fixed rate scheduling so more threads are not created
                            while (isWaitingForInput()) {
                                try {
                                    //To avoid a preventing the boolean from being changed, by calling get too often
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            setTimerValue(TIMER_DELAY_S); //run spawns a new thread besides the timer, which
                            return; //We do not want to call handleNewTurn since submit does this already
                        }
                        handleNewTurn();
                    }
                }
            }
        },1000,1000);
    }

    /**
     * This method is responsible for checking whether or we are waiting on input.
     * More specifically, if a JOptionPane is currently open, this will be true.
     * @return a boolean stating whether or input needs to be waited for
     */
    private synchronized boolean isWaitingForInput() {
        return this.waitingForInput;
    }

    /**
     * This method is responsible for setting whether or not the timer needs to wait on input
     * @param waitingForInput - whether or not timer should wait for input
     */
    public synchronized void setInputWait(boolean waitingForInput) {
        this.waitingForInput = waitingForInput;
    }

    /**
     * This allows the timer and the main thread to set the timervalue without worrying about
     * thread safety
     * @param value
     */
    private synchronized void setTimerValue(int value) {
        this.timerValue = value;
        System.out.println("Set to " + value);
    }

    /**
     * This allows the timer and the main thread to set the timervalue without worrying about
     * thread safety
     * @return an integer representing the timer value
     */
    private synchronized int getTimerValue() {
        return this.timerValue;
    }
}