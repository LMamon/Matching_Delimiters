import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Encapsulation {
    private String fileName;
    private BufferedReader reader;
    private int lineNumber = 1;
    private int charNumber =0;
    private boolean insideString =false;
    private boolean insideChar =false;
    private boolean insideSingleLineCo=false;
    private boolean insideMultiLineCo=false;

    //constructor with throw exception
     public Encapsulation( String fileName) throws FileNotFoundException {
        if (!new File(fileName).exists()){
            throw new FileNotFoundException(fileName +"does not exist");
        }
        this.fileName = fileName;
        this.reader = new BufferedReader(new FileReader(fileName));
    }

    public Character readFile(String fileName) throws IOException {
        int ch;
        while ((ch = reader.read()) != -1){
            char currChar = (char) ch;
            charNumber++;
            //handles new lines
            if (currChar == '\n'){
                lineNumber++;
                charNumber = 0;
                insideSingleLineCo = false;
            }

            //exclusion states
            if (!insideSingleLineCo && !insideMultiLineCo){
                if (!insideString && !insideChar) {
                    //check for comment start
                    if (currChar == '/' && (char) reader.read() == '/') {
                        insideSingleLineCo = true;
                    }
                    //check for start of char/string literals
                    else if (currChar == '"') {
                        insideString = true;
                    } else if (currChar == '\'') {
                        insideChar = true;
                    } else {
                        return currChar;
                    }
                } else {
                    //check for end of char/string literals
                    if (insideString && currChar == '"') {
                        insideString = false;
                    } else if (insideChar && currChar =='\'') {
                        insideChar = false;
                    }
                }
            } else if (insideMultiLineCo) {
                //check for multiline comment end
                if (currChar == '*' && (char) reader.read() == '/') {
                    insideMultiLineCo = false;
                }
            }
        }
        return null;
    }

    public String getCurrentPostion(){
         return "Line: " + lineNumber + " character #" + charNumber;
    }

}
