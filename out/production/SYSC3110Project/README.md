# SYSC3110Project

The authors of this project are John Khalife, Elyssa Grant, Sandy Alzabadani, and Gillian O'Connell.

This repository presently contains Milestone 2 of the SYSC 3110 project. This deliverable is comprised of a standard Java GUI-based version of Scrabble. This Scrabble game implements the MVC design pattern. This game supports two to four players. On a player's turn, the player will be able to place letters onto the board to create words and will be awarded points based on the letters used in the completed word. A simplified version of the game is used, which does not include blank tiles or premium squares.

Presently, this game lacks certain features that are yet to be implemented. Checks for menu items that will restart or quit the game are present in the Controller, but do not exist in the actual implementation.

The following is a breakdown of the details of future deliverables- outlining how the current implementation of the game will be expanded.

Deliverable 3 will expand upon the simplified game logic of Deliverables 1 and 2 by implementing blank tiles and premium squares. 'AI' players will also be included, allowing the user to play against the game instead of other people. The 'AI' players will be designed to use a strategy algorithm to determine their next move. The game will support multiple 'AI' players within a game.

Deliverable 4 will further expand upon the game logic. These changes will include a multi-level undo/redo feature for players. Additionally, players can save incomplete games and load them in the future to continue playing. Finally, the game will allow users to implement custom boards with varying placements of premium squares.

Overall, future deliverables aim to enhance the user experience by adding a more attractive interface and expanding the current features to offer a unique Scrabble game each time.


Known issues:
- Players can input a series of coordinates and then a series of letters if they want. The order they submit the coordinates must be the same order they submit the letters they want to add to those coordinates
- Players cannot take back a submission of a letter or coordinate aside from submitting an invalid word and trying again from scratch
- If a player adds several letters to the board and then presses "exchange", the letters will remain on the board visually until the next player to add a word to the board presses submit
