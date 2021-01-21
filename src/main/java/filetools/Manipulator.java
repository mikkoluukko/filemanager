package filetools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Manipulator {
    File folder;
    File textfile;
    ArrayList<String> filenames;
    ArrayList<String> filetypes = new ArrayList<String>();

    public Manipulator(String path) {
        this.folder = new File(path);
        this.filenames = new ArrayList<String>(Arrays.asList(folder.list()));
        for (String filename : filenames) {
            String extension = "";
            int i = filename.lastIndexOf('.');            
            extension = filename.substring(i+1);
            if (!filetypes.contains(extension)) {
                filetypes.add(extension);
            }
        }
    }

    public void listFilenames() {
        for (String filename : folder.list()) {
            System.out.println(filename);
        }
    }

    public void listFiletypes() {
        for (String filetype : filetypes) {
            System.out.println(filetype);
        }
    }

    public void listFilesByType(String filetype) {
        if (filetypes.contains(filetype)) {
            for (String filename : filenames) {
                int i = filename.lastIndexOf('.');            
                String extension = filename.substring(i+1);
                if (filetype.equals(extension)) {
                    System.out.println(filename);
                }
            }
        } else {
            System.out.println("Invalid file extension.");
        }
    }

    public boolean printFileInfo(String filename) {
        try {
            File textfile = new File(folder.getPath() + "/" + filename);
            // Use File class's length method to get long variable of the file's byte count
            long filesize = textfile.length();
            System.out.println("The file size is " + filesize + " bytes, or " + (int)Math.ceil(filesize/1024.0) + " KB.");
        } catch (Exception e) {
            System.out.println("not found");
        }
        

        // Use BufferedReader to be able to easily read the file line by line
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textfile))) {
            int lineCount = 0;
            while (bufferedReader.readLine() != null) {
                lineCount++;
            }
            System.out.println("The file has " + lineCount + " lines.");
            return true;
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public int countWordOccurrences(String filename, String inputWord) {
        // Initialize wordOccurrences to -1 as to represent case where file is not found
        int wordOccurrences = -1;
        try (Scanner scanner = new Scanner(new File(folder.getPath() + "/" + filename))) {
            wordOccurrences = 0;
            // Use any one or more non alphabet characters as delimiter 
            scanner.useDelimiter("[^a-zA-Z]+");
            String word;
            // Use pattern object to compile inputWord as a case insensitive regex
            Pattern p = Pattern.compile(inputWord, Pattern.CASE_INSENSITIVE);
            while(scanner.hasNext()){
                word = scanner.next();
                // Use matcher object on every word in the file to see if the word matches with the inputWord
                if (p.matcher(word).matches()) {
                    wordOccurrences++;
                }
            }            
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return wordOccurrences;
    }
}