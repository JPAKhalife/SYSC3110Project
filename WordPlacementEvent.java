import java.util.ArrayList;
import java.util.EventObject;

public class WordPlacementEvent extends EventObject {

    private String word;
    private int direction;
    private ArrayList<Letter> wordOfLetters;
    /**
     * Constructs an event for placing a word
     *
     * @param location The initial location we are trying to add the word to
     * @throws IllegalArgumentException if source is null
     */
    public WordPlacementEvent(String location, int direction) {
        super(location);
        wordOfLetters = new ArrayList<>(8);
        this.direction = direction;
    }

    public WordPlacementEvent(String location, int direction, String word, ArrayList<Letter> letters)
    {
        super(location);
        wordOfLetters = new ArrayList<>(letters);
        this.direction = direction;
        this.word = word;
    }

    /**
     * Default constructor for placing a word. Uses the center tile of the board as its initial location
     */
    public WordPlacementEvent()
    {
        super("7,7");
        wordOfLetters = new ArrayList<>(8);
    }

    public String getLocation()
    {
        return (String) this.getSource();
    }

    public int getDirection()
    {
        return direction;
    }

    public ArrayList<Letter> getLetters()
    {
        return (ArrayList<Letter>) wordOfLetters.clone();
    }

    public int wordLength()
    {
        return word.length();
    }
}
