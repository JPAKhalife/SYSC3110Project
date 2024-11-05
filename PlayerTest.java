import org.junit.Test;

import java.util.ArrayList;
import java.util.Dictionary;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getScore() {
        Player player = new Player();

        //initial score should always be zero
        assertEquals(player.getScore(), 0);

        //Adding a known value to the player's score
        player.updateScore(8);
        assertEquals(player.getScore(), 8);
    }

    @Test
    public void playerTurn() {
        LetterBag.createBag();
        Player player = new Player();

        ArrayList<Letter> initialRack = player.getRack();

        player.placeLetter(0);
        player.addCoordinate(0, 0);
        player.placeLetter(1);
        player.addCoordinate(0,1);
        player.placeLetter(2);
        player.addCoordinate(0,2);

        Dictionary<ArrayList<Letter>, ArrayList<String>> playerTurn = player.playerTurn(1);
        ArrayList<Letter> playedLetters = playerTurn.keys().nextElement();
        ArrayList<String> locations = playerTurn.elements().nextElement();

        for(int i = 0; i < playedLetters.size(); i++)
        {
            assertTrue(initialRack.contains(playedLetters.get(i)));
            //Assert the locations are correct as well
        }

        Dictionary<ArrayList<Letter>, ArrayList<String>> exchangedLetters = player.playerTurn(2);

    }

    @Test
    public void placeLetter() {
        LetterBag.createBag();
        Player player = new Player();

        //getting the rack so that the letter placed can be compared to the letter in the rack
        ArrayList<Letter> rack = player.getRack();
        player.placeLetter(0);
        player.placeLetter(1);
        player.placeLetter(2);
        player.placeLetter(3);
        player.placeLetter(4);
        player.placeLetter(5);

        //Need to assert the value on the rack and the value in the used letters are the same
        Dictionary<ArrayList<Letter>, ArrayList<String>> placedLetters = player.playerTurn(1);
        ArrayList<Letter> letters = placedLetters.keys().nextElement();

        for(int i = 0; i < letters.size(); i++)
        {
            assertEquals(rack.get(i).getLetter(), letters.get(i).getLetter());
        }
    }

    @Test
    public void addCoordinate() {
        Player player = new Player();

        for(int i = 1; i < 16; i++){
            for(int j = 1; j < 16; j++){
                player.addCoordinate(i, j);
            }
        }

        Dictionary<ArrayList<Letter>, ArrayList<String>> placedLetters = player.playerTurn(1);
        ArrayList<String> coordinates = placedLetters.elements().nextElement();

        for(int i =0; i < 16; i++)
        {
            for(int j = 0; j < 16; j++)
            {
                char rowLetter = (char)(i + 65); //turning the row number into the appropriate letter value
                String location = String.valueOf(rowLetter) + j;
                assertTrue(coordinates.contains(location));
            }
        }
    }

    @Test
    public void updateScore() {
        Player player = new Player();

        player.updateScore(5);
        assertEquals(5, player.getScore());
        player.updateScore(5);
        assertEquals(10, player.getScore());
        player.updateScore(-3);
        assertEquals(10, player.getScore());
        assertTrue(player.updateScore(-1));

    }

    @Test
    public void pullFromBag() {
        LetterBag.createBag();
        Player player = new Player();

        ArrayList<Letter> rack = player.getRack();
        player.placeLetter(0);
        player.updateScore(5);

        //ensuring that the rack has been updated
        boolean newvalue = false;
        for(int i = 0; i < rack.size(); i++)
        {
            if(rack.get(i).getLetter() != player.getRack().get(i).getLetter())
            {
                newvalue = true;
            }
        }
        assertTrue(newvalue);

        assertEquals(7, player.getRack().size());
    }

    @Test
    public void isRackEmpty() {
        Player player = new Player();

        //no bag, so should be an empty rack
        assertTrue(player.isRackEmpty());

        LetterBag.createBag();
        player.updateScore(1); //to pull from the bag
        assertFalse(player.isRackEmpty());
    }

    @Test
    public void getRack() {
        Player player = new Player();
        LetterBag.createBag();

        ArrayList<Letter> rack = player.getRack();

        assertNotNull(rack);
        assertEquals(7, rack.size());
    }
}