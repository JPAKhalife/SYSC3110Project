# SYSC3110Project

The authors of this project are John Khalife, Elyssa Grant, Sandy Alzabadani, and Gillian O'Connell.

This repository presently contains Milestone 4 of the SYSC 3110 project. This deliverable is a standard Java GUI-based version of Scrabble. This Scrabble game implements the MVC design pattern. This game supports two to four players. This version of the game supports AI players, to allow users to play against the game as opposed to other people or to add to the group of players. On a player's turn, the player will be able to place letters onto the board to create words and will be awarded points based on the letters used in the completed word. This game contains full Scrabble features, which include blank tiles for custom letter placement and premium squares for bonus points awarded depending on a word's location on the board. 

Additionally, this game includes features to enhance the digital format of the game. This implementation allows players to save the state of an incomplete game in a serialized format. This feature allows for this file to be loaded in the future to continue the game. As well, a multi-level undo and redo feature was implemented to allow the current player to undo and redo the placement of tiles before submitting the word. Finally, users will be able to load their own custom made boards, with varying premium tile placement, into the game for a unique Scrabble experience. 

Presently, checks for menu items that will restart or quit the game are present in the Controller, but do not exist in the actual implementation.

The following is a breakdown of the details of future deliverables- outlining how the current implementation of the game will be expanded.

The bonus Deliverable 5 will include a new feature to enhance the user experience. *INSERT BONUS FEATURE DESCRIPTION ONCE SELECTED*

Overall, this deliverable aims to provide a user-friendly and complete Scrabble experience to the users. 

Known issues:
There are no known issues with the current version of the game.

Notes for marker:
Board uses an inner-functions in the method initializeTiles() to override the default handler. This inner class is not highlighted in the UML diagram, as there is no known UML convention for representing inner-functions.  

