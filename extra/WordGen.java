import java.io.File;  // Import the File class
import java.io.FileWriter;  // Import this class to handle errors
import java.util.Scanner;

public class WordGen {

    public static void main(String[] args) {
        //Declare all the readers and writers and file objects      
        try {
            File inputFile = new File("words.txt");
            File outputFile = new File("Words.java");
            Scanner reader = new Scanner(inputFile);
            FileWriter writer = new FileWriter(outputFile);

            writer.write("public enum Words {\n");

        while (reader.hasNextLine()) {
            String word = reader.nextLine();
            String message = "    " + word.toUpperCase() + "(\"" + word + "\"),\n";
            writer.write(message);   
        }

        writer.write("\n\n    private final String word;");
        writer.write("\n\n    Words(String word) {\n");
        writer.write("    this.word = word;\n    }");

        writer.write("\n\n    public String getWord() {");
        writer.write("\n    return this.word;\n    }\n}");
        writer.close();
        reader.close();

        } catch (Exception e) {
            System.out.println("There was an error declaring file readers and writers.");
            e.printStackTrace();
        }
    }
}
