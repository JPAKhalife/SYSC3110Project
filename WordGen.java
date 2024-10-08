
import java.io.File;  // Import the File class
import java.io.FileWriter;  // Import this class to handle errors
import java.io.IOException;
import java.util.Scanner;

public class WordGen {
    
    File inputFile;
    File outputFile;
    Scanner reader;
    FileWriter writer;

    public static int main(String[] args) {
        
        //Declare all the readers and writers and file objects      
        try {
            File inputFile = new File("words.txt");
            File outputFile = new File("Words.java");
            Scanner reader = new Scanner(inputFile);
            FileWriter writer = new FileWriter(outputFile);
        } catch (Exception e) {
            System.out.println("There was an error declaring file readers and writers.");
            e.printStackTrace();
        }

        while (reader.hasNextLine()) {
            String word = reader.nextLine();
            String message = word.toUpperCase() + "(\"" + word + "\"),";
            try {
                writer.write(message);
            } catch (IOException e) {
                System.out.println("There was an error Writing to file.");
                e.printStackTrace();
            }
            
        }

        return 0;
    }
}
