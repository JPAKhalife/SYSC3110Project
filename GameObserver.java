public interface GameObserver {
    public void handleBoardUpdate(ErrorEvent e);
    public void handleScoreUpdate(int winner);
    public void handleScoreBoardUpdate();
    public void handleLetterPlacement(char y, int x, char letter);
}
