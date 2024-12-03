import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Dictionary;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;

    @Before
    public void setUp() throws Exception {
        LetterBag.createBag();
        player = new Player();
    }

    @After
    public void tearDown() throws Exception {
        LetterBag.emptyBag();
    }

    @Test
    public void getScore() {
        //initial score should always be zero
        assertEquals(player.getScore(), 0);

        //Adding a known value to the player's score
        player.updateScore(8);
        assertEquals(player.getScore(), 8);
    }

    @Test
    public void playerTurn() {
        ArrayList<Letter> initialRack = player.getRack();

        player.placeLetter(0);
        player.addCoordinate('a', 1);
        player.placeLetter(1);
        player.addCoordinate('a',1);
        player.placeLetter(2);
        player.addCoordinate('a',2);

        Dictionary<ArrayList<Letter>, ArrayList<String>> playerTurn = player.playerTurn(1);
        ArrayList<Letter> playedLetters = playerTurn.keys().nextElement();
        ArrayList<String> locations = playerTurn.elements().nextElement();

        for(int i = 1; i < playedLetters.size(); i++)
        {
            assertTrue(initialRack.contains(playedLetters.get(i)));
            //Assert the locations are correct as well
            assertEquals("a"+i, locations.get(i));
        }

        Dictionary<ArrayList<Letter>, ArrayList<String>> exchangedLetters = player.playerTurn(2);
        assertFalse(exchangedLetters.keys().hasMoreElements()); //Should return an empty arraylist of letters if exchanging
        assertFalse(exchangedLetters.elements().hasMoreElements());

    }

    @Test
    public void placeLetter() {
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
        for(char i = 'a'; i <= 'o'; i++){
            for(int j = 1; j < 16; j++){
                System.out.println(Character.toString(i) + j);
                player.addCoordinate(i, j);
            }
        }

        Dictionary<ArrayList<Letter>, ArrayList<String>> placedLetters = player.playerTurn(1);
        ArrayList<String> coordinates = placedLetters.elements().nextElement();

        for(int i = 0; i < 15; i++)
        {
            for(int j = 1; j < 16; j++)
            {
                char rowLetter = (char)(i + 97); //turning the row number into the appropriate letter value
                String location = String.valueOf(rowLetter) + j;
                assertTrue(coordinates.contains(location));
            }
        }
    }

    @Test
    public void updateScore() {
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
        LetterBag.emptyBag();
        Player player = new Player();
        //no bag, so should be an empty rack
        assertTrue(player.isRackEmpty());

        LetterBag.createBag();
        player.updateScore(1); //to pull from the bag
        assertFalse(player.isRackEmpty());
    }

    @Test
    public void getRack() {
        ArrayList<Letter> rack = player.getRack();

        assertNotNull(rack);
        assertEquals(7, rack.size());
    }
}