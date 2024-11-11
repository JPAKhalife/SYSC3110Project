import java.util.ArrayList;
import java.util.Dictionary;

public interface GameObserver {
    public void handleLetterPlacement(Dictionary<ArrayList<Letter>, ArrayList<String>> word);

    public void handleBoardUpdate(ErrorEvent e);
    public void handleScoreUpdate(int winner);
    public void handleNewTurn(int playerNum);
}
