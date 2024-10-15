import java.util.*;

/**
 * The LetterBag class contains the dictionary of letters and words to be used in the game
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani
 * @date 08/10/2024
 */
public class LetterBag {
    private Dictionary<Letter,Integer> letters;
    private List<Letter> letterList = new ArrayList<>(); // Maintain state across method calls
    private Random random = new Random();


    public void createBag(){
        letters = new Hashtable<Letter, Integer>();
        Letter letterA = new Letter('A',1);
        Letter letterB = new Letter('B',3);
        Letter letterC = new Letter('C',3);
        Letter letterD = new Letter('D',2);
        Letter letterE = new Letter('E',1);
        Letter letterF = new Letter('F',4);
        Letter letterG = new Letter('G',2);
        Letter letterH = new Letter('H',4);
        Letter letterI = new Letter('I',1);
        Letter letterJ = new Letter('J',8);
        Letter letterK = new Letter('K',5);
        Letter letterL = new Letter('L',1);
        Letter letterM = new Letter('M',3);
        Letter letterN = new Letter('N',1);
        Letter letterO = new Letter('O',1);
        Letter letterP = new Letter('P',3);
        Letter letterQ = new Letter('Q',10);
        Letter letterR = new Letter('R',1);
        Letter letterS = new Letter('S',1);
        Letter letterT = new Letter('T',1);
        Letter letterU = new Letter('U',1);
        Letter letterV = new Letter('V',4);
        Letter letterW = new Letter('W',4);
        Letter letterX = new Letter('X',8);
        Letter letterY = new Letter('Y',4);
        Letter letterZ = new Letter('Z',10);

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

     }

    public Letter getNextLetter(){
        if (letterList.isEmpty()) {
            return null; // No letters in the bag
        }

        int index = random.nextInt(letterList.size());
        Letter selectedLetter = letterList.get(index);

        int remainingCount = letters.get(selectedLetter) - 1;
        if (remainingCount > 0) {
            letters.put(selectedLetter, remainingCount);
        } else {
            letters.remove(selectedLetter);
        }

        letterList.remove(index); // Update the list to reflect the removal

        return selectedLetter;

    }
    
}
