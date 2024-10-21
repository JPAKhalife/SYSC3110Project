# SYSC3110Project
Project distribution (temp)
-John: Enum (Word), Letter, Display
-Elyssa: Player, Game
-Gillian: Board
-Sandy: LetterBag, Game

The authours of this project are John Khalife, Elyssa Grant, Sandy Alzabadani, and Gillian O'Connell.

This repositiory contains Milestone 1- a text-based implementation of Scrabble. In this deliverable,
a working model of the game is produced that will allow two to four players to play. On a player's turn, the player will be able to place letters onto the board to create words and will be awarded points based on the letters used in the completed word. Finally, deliverable 1 is the 'model' component for a MVC 

Future deliverables of this project will expand the features of the game and create a better user experience while playing the game. Some of these enhancments include a GUI displaying the game board's current state, premium and enhanced user features such as the ability to undo/redo letter placements, blank tiles, and premium squares. Additionally, futurue deliverables will include expanding the design model to be a complete MVC pattern.


Known issues:
- Exchanging letters with the bag requires players to start with the highest index and work their way down, to avoid IndexOutOfBounds exception
- An empty line is occasionally created during the player's input, which requires "Enter" to be pressed. This is done to avoid buffering issues where the player's input is overwritten by the newline character of their previous input
