/*Class that encapsulates input file and has methods to read and process the file
Louis, M,
*/
import java.io.*;

public class Encapsulation {
    private String fileName;
    private BufferedReader reader;
    public int lineNumber = 1;
    private int charNumber =0;
    private boolean insideString =false;
    private boolean insideChar =false;
    private boolean insideSingleLineCo=false;
    private boolean insideMultiLineCo=false;

    //constructor with throw exception
     public Encapsulation(String fileName) throws FileNotFoundException {
        if (!new File(fileName).exists()){
            throw new FileNotFoundException(fileName +" does not exist");
        }
        this.fileName = fileName;
        this.reader = new BufferedReader(new FileReader(fileName));
    }

    public Character readFile() throws IOException {
        int ch;
        while ((ch = reader.read()) != -1) {
            char currChar = (char) ch;
            charNumber++;

            //returns string of current line # and character number of current character
            System.out.println("Reading character: '" + currChar + "' at line " + lineNumber + ", character #" + charNumber);

            //handles new lines
            if (currChar == '\n') {
                lineNumber++;
                charNumber = 0;
                insideSingleLineCo = false;
            }
            //exclusion states
            if (!insideSingleLineCo && !insideMultiLineCo) {
                if (!insideString && !insideChar) {
                    //check for comment start
                    if (currChar == '/') {
                        reader.mark(1);
                        char nextChar = (char) reader.read();
                        if (nextChar == '/') {
                            insideSingleLineCo = true;
                        } else if (nextChar == '*') {
                            insideMultiLineCo = true;
                        } else {
                            reader.reset();
                            return currChar;
                        }
                        //check for start of char/string literals
                    } else if (currChar == '"') {
                        insideString = true;
                    } else if (currChar == '\'') {
                        insideChar = true;
                    } else if (currChar == '{' || currChar == '}' ||
                            currChar == '[' || currChar == ']' ||
                            currChar == '(' || currChar == ')') {
                        return currChar;
                    }
                } else {
                    //check for end of char/string literals
                    if (insideString && currChar == '"') {
                        insideString = false;
                    } else if (insideChar && currChar == '\'') {
                        insideChar = false;
                    }
                }
            } else if (insideMultiLineCo) {
                //check for multiline comment end
                if (currChar == '*') {
                    reader.mark(1);
                    char nextChar = (char) reader.read();
                    if (nextChar == '/') {
                        insideMultiLineCo = false;
                    } else {
                        reader.reset();
                    }
                }
            }
        }
        return null;
    }
    public String getCurrentPosition(){
         return "Line: " + lineNumber + " character #" + charNumber;
    }
}
