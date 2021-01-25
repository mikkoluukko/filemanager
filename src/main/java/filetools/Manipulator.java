package filetools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Manipulator {
    File folder;
    File textfile;
    ArrayList<String> filenames;
    ArrayList<String> filetypes = new ArrayList<String>();

    // Always read the resources folder for filenames and list their types when creating a Manipulator
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

    // In addition to printing file size return boolean so it won't try printFileLines or countWordOccurences if file not found
    public boolean printFileSize(String filename) {        
        File textfile = new File(folder.getPath() + "/" + filename);
        if (textfile.exists()) {            
            // Use File class's length method to get long variable of the file's byte count
            long filesize = textfile.length();
            System.out.println("The file size is " + filesize + " bytes, or " + (int)Math.ceil(filesize/1024.0) + " KB.");
            return true;
        } else {
            System.out.println("File not found or not accessible.");
            return false;
        }
    }
        
    public void printFileLineCount(String filename) {
        File textfile = new File(folder.getPath() + "/" + filename);
        // Use BufferedReader to easily read the file line by line
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textfile))) {
            int lineCount = 0;
            while (bufferedReader.readLine() != null) {
                lineCount++;
            }
            System.out.println("The file has " + lineCount + " lines.");
            bufferedReader.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Use Scanner to split the file into words and then process through it word by word.
    public int countWordOccurrences(String filename, String inputWord) {
        int wordOccurrences = 0;
        try (Scanner scanner = new Scanner(new File(folder.getPath() + "/" + filename))) {
            // Use any one or more non alphabet characters as delimiter 
            scanner.useDelimiter("[^a-zA-Z]+");
            String word;
            // Use pattern object to compile inputWord as a case insensitive regex pattern
            Pattern p = Pattern.compile(inputWord, Pattern.CASE_INSENSITIVE);
            while(scanner.hasNext()){
                word = scanner.next();
                // Use matcher object on every word in the file to see if the word matches with the inputWord
                if (p.matcher(word).matches()) {
                    wordOccurrences++;
                }
            }
            scanner.close();        
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return wordOccurrences;
    }

    // If my time logging is working correctly, this is the best optimized search I could come up with.
    // I guess at some point as the file size grows this wouldn't anymore be the fastest one because of the huge String object.
    public int fastSearch(String filename, String inputWord) {
        try
        {
            String content = new String ( Files.readAllBytes( Paths.get(folder.getPath() + "/" + filename) ) );
            Pattern p = Pattern.compile("[^a-zA-Z]+" + inputWord + "($|\\r|\\n|\\r\\n|[^a-zA-Z]+)", Pattern.CASE_INSENSITIVE);
            return (int)p.matcher(content).results().count();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return 0;
        }
    }

    // Tried to optimize search by using BufferedReader instead of Scanner. 
    public int fastSearch2(String filename, String inputWord) {
        // Initialize wordOccurrences to -1 as to represent case where file is not found
        int wordOccurrences = 0;
        inputWord.toLowerCase();
        File textfile = new File(folder.getPath() + "/" + filename);
        // Use BufferedReader to easily read the file line by line
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textfile))) {
            Pattern p = Pattern.compile(inputWord, Pattern.CASE_INSENSITIVE);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineWords = line.split("[^a-zA-Z]+");
                for (String word : lineWords) {
                    if (p.matcher(word).matches()) {
                        wordOccurrences++;
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        return wordOccurrences;
    }

    // Tried to optimize search by storing words and their occurrences in a HashMap.
    // Probably not a very good use for HashMap.
    public int fastSearch3(String filename, String inputWord) {
        int wordOccurrences = 0;
        inputWord.toLowerCase();
        HashMap<String, Integer> words = new HashMap<>();
        File textfile = new File(folder.getPath() + "/" + filename);
        // Use BufferedReader to easily read the file line by line
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textfile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineWords = line.split("[^a-zA-Z]+");
                for (String word : lineWords) {
                    word = word.toLowerCase();
                    if (!words.containsKey(word)) {
                        words.put(word, 1);
                    } else {
                        words.put(word, words.get(word) + 1);
                    }
                }
            }
            wordOccurrences = words.get(inputWord);
            bufferedReader.close();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        return wordOccurrences;
    }

    // Tried to implement word count using Stream<String> but couldn't get the matching working correctly.
    // public int fastSearch4(String filename, String inputWord) {
    //     AtomicInteger count2 = new AtomicInteger(0);
    //     inputWord.toLowerCase();
    //     Pattern p = Pattern.compile("[^a-zA-Z]+" + inputWord + "($|\\r|\\n|\\r\\n|[^a-zA-Z]+)", Pattern.CASE_INSENSITIVE);
    //     try (Stream<String> lines = Files.lines(Paths.get(folder.getPath() + "/" + filename))) {
    //         lines.forEach(l -> {
    //             l.split("[^a-zA-Z]+");
    //             count2.getAndAdd((int)p.matcher(l).results().count());
    //         });

    //         return count2.get();
    //     }
    //     catch (Exception ex) {
    //         System.out.println(ex.getMessage());
    //         return -1;
    //     }
    // }
}