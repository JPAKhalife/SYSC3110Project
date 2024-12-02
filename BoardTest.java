import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        //Make sure that the score returned by add word is 4 (double tile).
        assertEquals(board.addWord(letters,locations), 4);
        assertEquals(ErrorEvent.GameError.NONE,board.getStatus().getError());
    }

    @Test
    public void addWordTileTest() {
        ArrayList<Letter> letters = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        letters.add(new Letter('a',1));
        letters.add(new Letter('t',1));
        locations.add("h8");
        locations.add("i8");
        //Make sure that the score returned by add word is 4 (double word).
        assertEquals(board.addWord(letters,locations), 4);
        assertEquals(ErrorEvent.GameError.NONE,board.getStatus().getError());

        letters.clear();
        locations.clear();
        letters.add(new Letter('o',1));
        locations.add("i9");
        //Make sure that the score returned by add word is 3 (double letter).
        assertEquals(board.addWord(letters,locations), 3);
        letters.clear();
        locations.clear();
        locations.add("h9");
        locations.add("j9");
        letters.add(new Letter('w',1));
        letters.add(new Letter('n',1));
        board.addWord(letters,locations);
        letters.clear();
        locations.clear();
        locations.add("j10");
        letters.add(new Letter('a',1));
        //Make sure that the score returned by add word is 4 (triple letter).
        assertEquals(board.addWord(letters,locations), 4);
        letters.clear();
        locations.clear();
        locations.add("j11");
        locations.add("j12");
        locations.add("j13");
        locations.add("j14");
        locations.add("j15");
        letters.add(new Letter('m',1));
        letters.add(new Letter('i',1));
        letters.add(new Letter('b',1));
        letters.add(new Letter('i',1));
        letters.add(new Letter('a',1));
        assertNotEquals(board.addWord(letters,locations), -1);
        letters.clear();
        locations.clear();
        letters.add(new Letter('a',1));
        letters.add(new Letter('a',1));
        locations.add("h15");
        locations.add("i15");
        //make sure that tripple word works
        assertEquals(board.addWord(letters,locations), 9);
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
        //This copies the board
        int[][] originalBoard = Arrays.stream(board.getBoardTiles()).map(int[]::clone).toArray(int[][]::new);
        Board board2 = new Board("boardtest.xml");
        for (int i = 0 ; i < Board.BOARD_SIZE ; i++) {
            for ( int j = 0 ; j < Board.BOARD_SIZE ; j++) {
                assertNotEquals(originalBoard[i][j],board2.getBoardTiles()[i][j]);
            }
        }

    }
}
