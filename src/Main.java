import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static String getFileName(){
        Scanner input = new Scanner(System.in);
        String fileName;
        while(true){
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

    public static void main(String[] args) throws FileNotFoundException {
        String fileName = getFileName();
        Encapsulation test = new Encapsulation(fileName);
        System.out.println("loaded file.");


    }
}