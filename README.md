# SYSC3110Project

The authors of this project are John Khalife, Elyssa Grant, Sandy Alzabadani, and Gillian O'Connell.

This repository presently contains the bonus Milestone 5 of the SYSC 3110 project. This deliverable is a standard Java GUI-based version of Scrabble. This Scrabble game implements the MVC design pattern. This game supports two to four players. This version of the game supports AI players, to allow users to play against the game as opposed to other people or to add to the group of players. On a player's turn, the player will be able to place letters onto the board to create words and will be awarded points based on the letters used in the completed word. This game contains full Scrabble features, which include blank tiles for custom letter placement and premium squares for bonus points awarded depending on a word's location on the board. 

Additionally, this game includes features to enhance the digital format of the game. This implementation allows players to save the state of an incomplete game in a serialized format. This feature allows for this file to be loaded in the future to continue the game. As well, a multi-level undo and redo feature was implemented to allow the current player to undo and redo the placement of tiles before submitting the word. Finally, users will be able to load their own custom made boards, with varying premium tile placement, into the game for a unique Scrabble experience. Finally, the game supports a timer mode, which constraints each player's turn to 30 seconds. Once the timer reaches zero, the game automatically moves on to the next player's turn. 

Presently, checks for menu items that will restart or quit the game are present in the Controller, but do not exist in the actual implementation.

The following is a breakdown of the details of future deliverables- outlining how the current implementation of the game will be expanded.

The bonus Deliverable 5 will include a timer feature to enhance the user experience. Each user will have 30 seconds to perform their turn. Once the timer reaches 0, the game will automatically move on to the next player's turn. This timer feature is optional. By default, the game will not include the turn timer and can be enabled by selecting the timer item in the 'Game Options' menu bar. 

Overall, this deliverable aims to provide a user-friendly and complete Scrabble experience to the users. 

Known issues:
-If the user selects to save/import a file for the serializable feature, the user must select a file. This game does not support a cancel feature for the dialogue file selector

Notes for marker:
Board uses an inner-functions in the method initializeTiles() to override the default handler. This inner class is not highlighted in the UML diagram, as there is no known UML convention for representing inner-functions.  

