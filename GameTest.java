import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class GameTest {

    @Test
    public void getCurrentPlayer() {
        Game game = new Game();
        game.addPlayer();
        game.addPlayer();

        //checking to make sure the player obtained is different after turn changes
        Player player1 = game.getCurrentPlayer();
        game.handleNewTurn();
        Player player2 = game.getCurrentPlayer();
        assertNotSame(player1, player2);

        //since only two players, the next turn should be player 1 again
        game.handleNewTurn();
        Player player3 = game.getCurrentPlayer();
        assertSame(player1, player3);
    }

    @Test
    public void getPlayers() {
        Game game1 = new Game();
        Game game2 = new Game();

        //ensuring that the players list is not null
        assertNotNull(game1.getPlayers());
        assertNotNull(game2.getPlayers());

        //Initial should be an empty arraylist
        assertEquals(0, game1.getPlayers().size());

        //adding different numbers of players to both games
        game1.addPlayer();
        game1.addPlayer();
        game1.addPlayer();
        game2.addPlayer();
        //checking the sizes have changed
        assertEquals(game1.getPlayers().size(), 3);
        assertEquals(game2.getPlayers().size(), 1);

        //Ensuring that the arraylist returned is a copy, not the same memory reference
        ArrayList<Player> list1 = game1.getPlayers();
        ArrayList<Player> list2 = game1.getPlayers();
        assertNotSame(list1, list2);
    }

    @Test
    public void getBoard() {
        Game game = new Game();

        assertNotNull(game.getBoard());
    }

    @Test
    public void addPlayer() {
        //Should be 0 players at start
        Game game = new Game();
        assertEquals(0, game.getPlayers().size());
        //adding players
        game.addPlayer();
        assertEquals(1, game.getPlayers().size());
        game.addPlayer();
        assertEquals(2, game.getPlayers().size());
        game.addPlayer();
        assertEquals(3, game.getPlayers().size());

        //Ensuring the players are unique
        ArrayList<Player> arr = game.getPlayers();
        for(int i = 1; i < arr.size(); i++)
        {
            assertNotNull(arr.get(i));
            assertNotSame(arr.get(i - 1), arr.get(i));
        }
    }

    @Test
    public void findWinner() {
        Game game = new Game();
        game.addPlayer();
        game.addPlayer();
        game.addPlayer();
        game.addPlayer();

        //At the moment, everyone should have the same score --> Player 0 wins by default
        assertEquals(0, game.findWinner());

        //Artifically adding scores to Player 0 so they "win"
        game.getCurrentPlayer().updateScore(1);
        assertEquals(0, game.findWinner());

        //Adding a higher score to Player 1 so they win now
        game.handleNewTurn();
        game.getCurrentPlayer().updateScore(5);
        assertEquals(1, game.findWinner());

        //giving player 3 a higher score
        game.handleNewTurn();
        game.handleNewTurn();
        game.getCurrentPlayer().updateScore(8);
        assertEquals(3, game.findWinner());

        //giving player 2 the highest score and ensuring that it is not their turn when the score is added
        game.handleNewTurn();
        game.handleNewTurn();
        game.handleNewTurn();
        game.getCurrentPlayer().updateScore(12);
        game.handleNewTurn();
        assertEquals(2, game.findWinner());
    }

    @Test
    public void addWord() {

    }

    @Test
    public void addView() {
    }

    @Test
    public void removeView() {
    }

    @Test
    public void handleNewTurn() {
        Game game = new Game();
        game.addPlayer();
        game.addPlayer();
        game.addPlayer();
        GameObserver view = new ScrabbleView();

        //Ensuring all the players are unique
        Player p1 = game.getCurrentPlayer();
        game.handleNewTurn();
        Player p2 = game.getCurrentPlayer();

        assertNotSame(p1, p2);
        game.handleNewTurn();
        Player p3 = game.getCurrentPlayer();

        assertNotSame(p2, p3);
        assertNotSame(p1, p3);

        //Ensuring the player rolls over to the first player after the number of players has been met
        game.handleNewTurn();
        Player p4 = game.getCurrentPlayer();
        assertSame(p1, p4);

        //Ensure that the new view was updated
        game.addView(view);
        //INCOMPLETE
    }

    @Test
    public void handleBoardError() {
    }
}