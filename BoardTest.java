import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private ArrayList<Letter> letters;
    private ArrayList<String> locations;

    @Before
    public void setUp() throws Exception {
        board = new Board("board.xml");
        letters = new ArrayList<>();
        locations = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * This method is intended to test a successful scenario of adding a word.
     */
    @Test
    public void addWordSuccess() {
        ArrayList<Letter> letters = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h8");
        locations.add("h9");
        //Make sure that the score returned by add word is 2.
        assertEquals(board.addWord(letters,locations), 2);
        assertEquals(ErrorEvent.GameError.NONE,board.getStatus().getError());
    }

    @Test
    public void addWordTripleWord() {

    }

    @Test
    public void addWordDoubleLetter() {

    }


    @Test
    public void addWordTripleLetter() {

    }


    /**
     * This method ensures that adding a first word to the board not in the starting position returns a score of 01.
     * It also checks the error returned to make sure that is why -1 was returned and not some other reason.
     */
    @Test
    public void checkFirstWordWrongPosition() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h9");
        locations.add("h10");
        //Make sure that the score returned by add word is -1.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.FIRST_WORD_NOT_TOUCHING_CENTER,board.getStatus().getError());
    }

    /**
     * This method checks to make sure board can detect if letters were given without a location and vice versa.
     */
    @Test
    public void checkUnequalListLength() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h9");
        //Make sure that the score returned by add word is -1.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.UNEQUAL_LIST_LENGTH,board.getStatus().getError());
    }

    /**
     * This method tests whether or not addword can detect when letters are placed in a spot they should not be.
     */
    @Test
    public void checkInvalidPlacement() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h8");
        locations.add("h9");
        board.addWord(letters,locations);

        //Now add another word with the same positions
        //Make sure that the score returned by add word is -1.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.INVALID_PLACEMENT,board.getStatus().getError());

    }

    /**
     * This method tests to make sure an error happens when first word placed has <=1 letters
     */
    @Test
    public void checkFirstWordInvalidLength() {
        letters.add(new Letter('a',1));
        locations.add("h9");
        //Make sure that the score returned by add word is -1.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.FIRST_WORD_INVALID_LENGTH,board.getStatus().getError());
    }

    /**
     * This method checks to make sure that words are not connected
     */
    @Test
    public void checkWordNotConnected() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h8");
        locations.add("h9");
        board.addWord(letters,locations);
        //Make a new word not connected
        locations.clear();
        locations.add("h12");
        locations.add("h13");
        board.addWord(letters,locations);
        //Make sure that the score returned by add word is -1.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.WORD_NOT_CONNECTED,board.getStatus().getError());
    }

    /**
     * This test checks to make sure if letters are not placed on the same axis
     */
    @Test
    public void checkLettersNotStraight() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h8");
        locations.add("i10");
        //Make sure that the score returned by add word is -1.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.LETTERS_NOT_STRAIGHT,board.getStatus().getError());
    }

    /**
     * This test method checks to make sure that the board knows when letters are not adjacent
     */
    @Test
    public void checkLettersNotAdjacent() {
        letters.add(new Letter('a',1));

        letters.add(new Letter('a',1));
        letters.add(new Letter('r',1));
        letters.add(new Letter('o',1));
        letters.add(new Letter('n',1));
        locations.add("h8");

        locations.add("h11");
        locations.add("h11");
        locations.add("h12");
        locations.add("h13");
        //Make a new word.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.LETTERS_NOT_ADJACENT,board.getStatus().getError());
    }

    /**
     * This test method checks to make sure that the invalid word error is returned
     */
    @Test
    public void checkInvalidWord() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        letters.add(new Letter('r',1));
        letters.add(new Letter('o',1));
        letters.add(new Letter('n',1));
        letters.add(new Letter('a',1));
        locations.add("h8");
        locations.add("h9");
        locations.add("h10");
        locations.add("h11");
        locations.add("h12");
        locations.add("h13");
        //Make a new word.
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.INVALID_WORD,board.getStatus().getError());
    }

    /**
     * This method exists to check whether an error is thrown if
     * a word placed perpendicular to another word does not form a new word.
     */
    @Test
    public void checkInvalidIntersection() {
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        letters.add(new Letter('r',1));
        letters.add(new Letter('o',1));
        letters.add(new Letter('n',1));
        locations.add("h7");
        locations.add("h8");
        locations.add("h9");
        locations.add("h10");
        locations.add("h11");
        //Make sure there is no error adding this word.
        assertNotEquals(-1,board.addWord(letters,locations));
        assertEquals(ErrorEvent.GameError.NONE,board.getStatus().getError());
        locations.clear();
        letters.clear();
        //Now add a valid word that does not create a word to one perpendicular to it.
        letters.add(new Letter('f',1));
        letters.add(new Letter('a',1));
        locations.add("i11");
        locations.add("i12");
        assertEquals(board.addWord(letters,locations), -1);
        //Make sure that the error code corresponds to this.
        assertEquals(ErrorEvent.GameError.INVALID_INTERSECTION,board.getStatus().getError());
    }

    @Test
    public void checkBoardCustomTiles() {

    }
}
