public interface GameObserver {
    void handleLetterPlacement(char y, int x, char letter);

    public void handleBoardUpdate(ErrorEvent e);
    public void handleScoreUpdate(int winner);
    public void handleNewTurn(int playerNum);
}
