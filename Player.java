/**
 * This class is responsible for each player's information as well as the actions they perform in a turn.
 * @author Elyssa
 * @date 2024/10/08
 */
import java.util.*;

public class Player {
    private ArrayList <Letter> rack;
    private int score;
    private Scanner scan;

    /**
     * Constructor for the Player class
     */
    public Player()
    {
        rack = new ArrayList<>();
        score = 0;
        scan = new Scanner(System.in);
    }

    /**
     * Getter for the score value so that Game can determine the winner
     * @return the player's score
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Plays one turn of scrabble using the player's rack
     */
    public Dictionary<ArrayList<Letter>, ArrayList<String>> playerTurn()
    {
        String userTurn; //stores the turn type the user wants to perform
        int numLettersToPlay; //stores the number of letters the user wants to play
        int letterToPlay; //stores the actual letters the user plays
        Dictionary<ArrayList<Letter>, ArrayList<String>> playerWord = new Hashtable<>(); //Stores the scrabble notation for where the user wants to add things to the board
        ArrayList<Integer> usedValues = new ArrayList<>();
        ArrayList<Letter> letters = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        int turnScore = 0;

        //printing the player's rack
        System.out.print("Player's rack: \n");
        for(int i = 0; i<rack.size(); i++)
        {
            System.out.println(i+". "+rack.get(i));
        }

        System.out.println("1. Place Letters\n 2. Exchange Letters\n 3.Pass turn");
        userTurn = scan.nextLine();

        if(userTurn.equals("1")) //The user wants to place letters on the board
        {
            //Ask the user for a letter and placement
            System.out.println("Please select the number of letters you would like to place on the board");
            numLettersToPlay = scan.nextInt();
            for(int i = 0; i < numLettersToPlay; i++) {
                System.out.println("Please enter the index of the letter to play");
                letterToPlay = scan.nextInt();
                if(usedValues.contains(letterToPlay))
                {
                    System.out.println("Already used that letter. Try again\n");
                    continue; //TO BE DONE: Place this entire thing in a try-catch loop, and then put that in a while loop
                }
                else
                {
                    letters.add(rack.get(letterToPlay));
                    System.out.println("Using scrabble notation of [Col][Row], input the location of the letter");
                    locations.add(scan.nextLine());
                }

                playerWord.put(letters, locations);
            }
        }
        else if(userTurn.equals("2")) { //The user wants to exchange letters with the letter bag
            System.out.print("Please enter the number of letters you would like to exchange: ");
            numLettersToPlay = scan.nextInt();

            for (int i = 0; i < numLettersToPlay; i++) {
                System.out.println("Enter letter index to be exchanged: ");
                letterToPlay = scan.nextInt();
                Letter tempLetter = rack.remove(letterToPlay);

                LetterBag.addLetter(tempLetter);
            }

            pullFromBag();
        }

        return playerWord;
    }

    /**
     * Updates the player's score after they have played a round of scrabble
     * @param turnScore The score the player accumulated during that turn of play
     */
    private void updateScore(int turnScore)
    {
        score+= turnScore;
    }

    /**
     * Pulls more letters from the bag to fill their rack up to 7 letters
     */
    public boolean pullFromBag()
    {
        while(rack.size() < 7)
        {
            Letter letter = LetterBag.getNextLetter();
            if(letter != null)
            {
                rack.add(letter);
            }
            else
            {
                return false;
            }
        }

        return true;
    }
}
