public interface GameObserver {
    public void handleBoardUpdate(ErrorEvent e);
    public void handleScore(Player winner);
}
