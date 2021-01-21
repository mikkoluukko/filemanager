import java.util.Scanner;
import filetools.*;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manipulator fileManipulator = new Manipulator("src/main/resources");
        Logger logger = new Logger("src/main/logs/log.txt");
        System.out.println("Welcome to File System Manager");
        while (true) {
            System.out.println("Enter a number to select an action:");
            System.out.println("1 - List all the file names in the resources directory");
            System.out.println("2 - List all the file names of a specific file type");
            System.out.println("3 - Examine any txt file in the resources directory");
            System.out.println("0 - Exit");
            int mainCommand = Integer.parseInt(scanner.nextLine());
            if (mainCommand == 0) {
                System.out.println("Goodbye.");
                break;
            } else if (mainCommand == 1) {
                System.out.println("Listing all the file names in the resources directory:");
                fileManipulator.listFilenames();
            } else if (mainCommand == 2) {
                System.out.println("File types present in the resources directory:");
                fileManipulator.listFiletypes();
                System.out.println("Enter file extension to be listed or 0 to return to main menu:");
                String secondaryCommand = scanner.nextLine();
                if (secondaryCommand.equals("0")) {
                    break;
                } else {
                    fileManipulator.listFilesByType(secondaryCommand);
                }                
            } else if (mainCommand == 3) {
                System.out.println("Enter name of the text file to be examined (for example, \"Dracula.txt\"):");
                String filename = scanner.nextLine();
                System.out.println("Information about the txt file:");
                fileManipulator.printFileInfo(filename);
                System.out.println("Enter a word to search for it in the file or 0 to return to main menu:");
                String secondaryCommand = scanner.nextLine();
                if (secondaryCommand.equals("0")) {
                    break;
                } else {
                    int wordOccurrences = fileManipulator.countWordOccurrences(filename, secondaryCommand);
                    if (wordOccurrences != -1) {
                        System.out.println("The word " + secondaryCommand + " appears " + wordOccurrences + " times.");
                    }                    
                }                
            }
        }
        scanner.close();
    }
}