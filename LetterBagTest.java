import org.junit.Test;

import static org.junit.Assert.*;

public class LetterBagTest {

    @Test
    public void createBag() {
        assertNull(LetterBag.getNextLetter()); //Letterbag not yet filled --> should return nothing

        LetterBag.createBag();

        for(int i = 0; i < 98; i++)
        {
            assertNotNull(LetterBag.getNextLetter());
        }

        assertNull(LetterBag.getNextLetter()); //Should have exhausted all the letters in the list
    }

    @Test
    public void getNextLetter() {
        LetterBag.createBag();
        Letter l = LetterBag.getNextLetter();
        assertNotNull(l);
        assertTrue((l.getPoints() > 0) && (l.getPoints() < 11)); //points should be between 1 and 10
        assertTrue((l.getLetter() < 123) && (l.getLetter() > 96)); //Should give a lowercase letter --> using asciis to make sure in range
    }

    @Test
    public void addLetter() {
        assertNull(LetterBag.getNextLetter()); //Making sure that the letters don't carry over from previous test
        LetterBag.addLetter(new Letter('a', 1));
        assertEquals('a', LetterBag.getNextLetter().getLetter());
        assertNull(LetterBag.getNextLetter()); //Ensuring the letter was removed from the bag
    }
}