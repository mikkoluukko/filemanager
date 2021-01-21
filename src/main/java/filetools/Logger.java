package filetools;

import java.io.FileWriter;
import java.io.Writer;

public class Logger {
    Writer fileWriter;

    public Logger(String filepath) {
        try {
            fileWriter = new FileWriter(filepath, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    public void logResults() {
        System.out.println("log testing");
    }
}
