# SYSC3110Project

The authors of this project are John Khalife, Elyssa Grant, Sandy Alzabadani, and Gillian O'Connell.

This repository presently contains Milestone 1 of the SYSC 3110 project. This deliverable is comprised of a simplified, text-based implementation of Scrabble. This Scrabble game is the 'model' component of the MVC design pattern. This game supports two to four players. On a player's turn, the player will be able to place letters onto the board to create words and will be awarded points based on the letters used in the completed word. A simplified version of the game is used, which does not include blank tiles or premium squares.

Presently, this game contains an imperfect implementation of the game logic. When a player exchanges letters on his/her rack, exchanges must be made from the letter at the highest index to the lowest. If this is not adhered to, the indexes of the other letters will shift down, which may result in IndexOutOfBounds errors being thrown. Additionally, players are not limited to placing letters that connect to other words already on the board, nor is the first word expected to be placed in the middle of the board. 

The following is a breakdown of the details of future deliverables- outlining how the current implementation of the game will be expanded.

Deliverable 2 will use the game logic of Deliverable 1, but will include a Graphical User Interface. With this change, the Scrabble game will adhere to the complete MVC design pattern. Additionally, a thorough unit testing module will be created to ensure the game operates as intended.

Deliverable 3 will expand upon the simplified game logic of Deliverables 1 and 2 by implementing blank tiles and premium squares. 'AI' players will also be included, allowing the user to play against the game instead of other people. The 'AI' players will be designed to use a strategy algorithm to determine their next move. The game will support multiple 'AI' players within a game.

Deliverable 4 will further expand upon the game logic. These changes will include a multi-level undo/redo feature for players. Additionally, players can save incomplete games and load them in the future to continue playing. Finally, the game will allow users to implement custom boards with varying placements of premium squares.

Overall, future deliverables aim to enhance the user experience by adding a more attractive interface and expanding the current features to offer a unique Scrabble game each time.


Known issues:
- Exchanging letters with the bag requires players to start with the highest index and work their way down, to avoid IndexOutOfBounds exception
- An empty line is occasionally created during the player's input, which requires "Enter" to be pressed. This is done to avoid buffering issues where the player's input is overwritten by the newline character of their previous input
- No error-checking is done on the inputs given by the players
