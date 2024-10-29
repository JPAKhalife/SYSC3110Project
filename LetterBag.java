import java.util.*;

/**
 * The LetterBag class contains the dictionary of letters and words to be used in the game
 * @author Sandy Alzabadani
 * @date 08/10/2024
 */
public class LetterBag {
    private static Dictionary<Letter,Integer> letters;
    private static List<Letter> letterList = new ArrayList<>(); // Maintain state across method calls
    private static Random random = new Random();



    /**
     * Static method to create the letter bag's initial contents. Similar to a constructor, but static
     */
    public static void createBag(){
        letters = new Hashtable<Letter, Integer>();
        Letter letterA = new Letter('a',1);
        Letter letterB = new Letter('b',3);
        Letter letterC = new Letter('c',3);
        Letter letterD = new Letter('d',2);
        Letter letterE = new Letter('e',1);
        Letter letterF = new Letter('f',4);
        Letter letterG = new Letter('g',2);
        Letter letterH = new Letter('h',4);
        Letter letterI = new Letter('i',1);
        Letter letterJ = new Letter('j',8);
        Letter letterK = new Letter('k',5);
        Letter letterL = new Letter('l',1);
        Letter letterM = new Letter('m',3);
        Letter letterN = new Letter('n',1);
        Letter letterO = new Letter('o',1);
        Letter letterP = new Letter('p',3);
        Letter letterQ = new Letter('q',10);
        Letter letterR = new Letter('r',1);
        Letter letterS = new Letter('s',1);
        Letter letterT = new Letter('t',1);
        Letter letterU = new Letter('u',1);
        Letter letterV = new Letter('v',4);
        Letter letterW = new Letter('w',4);
        Letter letterX = new Letter('x',8);
        Letter letterY = new Letter('y',4);
        Letter letterZ = new Letter('z',10);

        letters.put(letterA,9);
        letters.put(letterB,2);
        letters.put(letterC,3);
        letters.put(letterD,4);
        letters.put(letterE,5);
        letters.put(letterF,6);
        letters.put(letterG,7);
        letters.put(letterH,8);
        letters.put(letterI,9);
        letters.put(letterJ,2);
        letters.put(letterK,3);
        letters.put(letterL,4);
        letters.put(letterM,5);
        letters.put(letterN,6);
        letters.put(letterO,7);
        letters.put(letterP,8);
        letters.put(letterQ,9);
        letters.put(letterR,2);
        letters.put(letterS,5);
        letters.put(letterT,6);
        letters.put(letterU,7);
        letters.put(letterV,8);
        letters.put(letterW,9);
        letters.put(letterX,2);
        letters.put(letterY,3);
        letters.put(letterZ,4);


        for(Enumeration<Letter> e = letters.keys();e.hasMoreElements();) {//loop through the enumeration of letter keys
            Letter letter = e.nextElement(); //get the next element
            int number = letters.get(letter); //get the count of the letter from the dictionary
            for (int i = 0; i < number; i++) {//add each letter key into the letter list
                letterList.add(letter);
            }
        }

    }


    /**
     * Method to pull a single letter from the bag
     * @return A letter from the bag
     */

    public static Letter getNextLetter(){
        if (letterList.isEmpty()) {
            return null; // No letters in the bag
        }

        int index = random.nextInt(letterList.size()); //get a random index
        Letter selectedLetter = letterList.get(index); //use the random index to get a random letter from the list

        int remainingCount = letters.get(selectedLetter) - 1;
        if (remainingCount > 0) { //if the remaining count of the letter is not zero
            letters.put(selectedLetter, remainingCount); //update the count of the letter in the dictionary
        } else {
            letters.remove(selectedLetter); //if the count is zero remove the letter key from the dictionary

        }

        letterList.remove(index); // Update the list to reflect the removal

        return selectedLetter;

    }

    /**
     * Adds a letter back into the bag
     * @param tempLetter The letter to be returned to the bag
     */
    public static void addLetter(Letter tempLetter) {
        letters.put(tempLetter, letters.get(tempLetter) + 1);
    }
}

