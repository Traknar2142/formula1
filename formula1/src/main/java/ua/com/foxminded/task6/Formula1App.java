package ua.com.foxminded.task6;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ua.com.foxminded.task6.formula1.Formula1;

public class Formula1App {

    public static void main(String[] args) {
        Path start = Paths.get("src/main/resources/start-1.log");
        Path end = Paths.get("src/main/resources/end-2.log");
        Path abbreviations = Paths.get("src/main/resources/abbreviations-2.txt");     
        Formula1 formula1 = new Formula1();
        try {
            System.out.println(formula1.makeTableResult(abbreviations,start,end)); 
        }catch (IOException e) {
            System.err.println("File not found " + e.getMessage());
            e.printStackTrace();
        }
        
        
    }
}
