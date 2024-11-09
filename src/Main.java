/*contain the main method. It should read in the file name from the
keyboard until a valid file name is entered and then create an object of the first class.
Louis, M,
*/
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;
import java.util.Scanner;

public class Main {
    public static String getFileName() {
        Scanner input = new Scanner(System.in);
        String fileName;
        while (true) {
            System.out.println("Enter filename: ");
            fileName = input.nextLine();
            try {
                Encapsulation fileReader = new Encapsulation(fileName);
                return fileName;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class DelimiterPosition {
        char delimiter;
        int line;

        DelimiterPosition(char delimiter, int line) {
            this.delimiter = delimiter;
            this.line = line;
        }
    }

    public static boolean checkDelimiters(Encapsulation fileReader) throws IOException {
        Stack<DelimiterPosition> delimiters = new Stack<>();
        Character currChar;
        boolean isValid = true;

        //read file character by character
        while ((currChar = fileReader.readFile()) != null) {
            int currentLine = fileReader.lineNumber;

            //stack opening delimters
            if (currChar == '{' || currChar == '[' || currChar == '(') {
                // Push the delimiter and its line number onto the stack
                delimiters.push(new DelimiterPosition(currChar, currentLine));
            }

            //handling closing delimiters
            else if (currChar == '}' || currChar == ']' || currChar == ')') {
                if (!delimiters.isEmpty()) {
                    DelimiterPosition top = delimiters.peek();

                    //if mismatched, check if it's a missing closing or opening delimiter
                    if (!typeMatch(top.delimiter, currChar)) {
                        //check stack parity (even/odd)
                        if (delimiters.size() % 2 == 1) {
                            // Stack has an odd number of elements => missing closing delimiter
                            System.out.println("Error: Missing opening delimiter '" + getOpeningDelim(currChar) + "' for '" +
                                    currChar + "' at line " + currentLine);
                        } else {
                            // Stack has an even number of elements => missing opening delimiter
                            System.out.println("Error: Missing closing delimiter '" + getClosingDelim(top.delimiter) + "' for closing '" +
                                    top.delimiter + "' at line " + top.line);
                        }
                        isValid = false;
                        return false;
                    } else {
                        // Correct match, pop the stack
                        delimiters.pop();
                    }
                }
            }
        }
        //after processing, check if there are unmatched opening delimiters left in the stack
        while (!delimiters.isEmpty()) {
            DelimiterPosition unmatched = delimiters.pop();
            System.out.println("Error: Missing closing delimiter for opening '" + unmatched.delimiter + "' at line " + unmatched.line);
            isValid = false;
        }
        return isValid;
    }

    private static boolean typeMatch(char open, char close) {
        return (open == '{' && close == '}') ||
                (open == '[' && close == ']') ||
                (open == '(' && close == ')');
    }

    private static char getClosingDelim(char openingDelimiter) {
        return switch (openingDelimiter) {
            case '{' -> '}';
            case '(' -> ')';
            case '[' -> ']';
            default -> openingDelimiter; // Just in case
        };
    }

    private static char getOpeningDelim(char closingDelimiter) {
        return switch (closingDelimiter) {
            case '}' -> '{';
            case ')' -> '(';
            case ']' -> '[';
            default -> ' '; // Just in case
        };
    }

    public static void main(String[] args) throws FileNotFoundException {
        String fileName = getFileName();
        try {
            Encapsulation test = new Encapsulation(fileName);
            if (checkDelimiters(test)) {
                System.out.println("\tDelimiters match");
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }

}