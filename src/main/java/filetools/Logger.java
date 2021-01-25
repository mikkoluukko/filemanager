package filetools;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    Writer fileWriter;

    // Initialize the FileWriter with the given path and set it to append to a file if it already exists.
    public Logger(String filepath) {
        try {
            fileWriter = new FileWriter(filepath, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Use FileWriter to write to log file. File needs to be closed with the separate method closeLogger().

    public void logResults(String word, int wordOccurrences, long executionTime) {
        try {
            // Use SimpleDateFormat to format the date to match the one in task assignment example output
            SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy'T'HH:mm");
            Date date = new Date();
            fileWriter.write(formatter.format(date) 
                + ": The term \"" + word + "\" was found " + wordOccurrences + " times. The function took " 
                + executionTime +  "ms to execute\n");
    
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void closeLogger() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
