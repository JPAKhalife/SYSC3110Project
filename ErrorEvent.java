public class ErrorEvent {
    private GameError error;

    /**
     * This enum documents every single error that can occur as a result of player input to the board.
     */
    public enum GameError {
        NONE(""),
        UNEQUAL_LIST_LENGTH("Word size and location size are different."),
        INVALID_PLACEMENT("A letter does not have a valid placement."),
        FIRST_WORD_INVALID_LENGTH("The first word placed must have 2 or more letters."),
        FIRST_WORD_NOT_TOUCHING_CENTER("The first word placed must touch square H8."),
        WORD_NOT_CONNECTED("The letters played are not connected to another word."),
        LETTERS_NOT_STRAIGHT("The letters specified are not all in the same row or column."),
        LETTERS_NOT_ADJACENT("The letters specified are not connected to each other."),
        INVALID_WORD("The letters added do not form a word."),
        INVALID_INTERSECTION("One of the letters added intersects a word but does not create a new one.");


        private final String errorDescription;

        /**
         * This is the getter method to return the error description
         * @return The error description
         */
        public String getErrorDescription() {
            return errorDescription;
        }

        /**
         * This is the constructor for creating a new constant
         * @param description The description of the error
         */
        private GameError(String description) {
            errorDescription = description;
        }
    };

    //This is the constructor for the error event
    public ErrorEvent() {
        this.error = GameError.NONE;
    }

    /**
     * This method gets the error stored in the error event
     * @return A game error
     */
    public GameError getError() {
        return error;
    }

    /**
     * This method sets the error of the event object.
     * @param error - the error that the event will be set to.
     */
    public void setError(GameError error) {
        this.error = error;
    }






}