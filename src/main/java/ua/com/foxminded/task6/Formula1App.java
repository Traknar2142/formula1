package ua.com.foxminded.task6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ua.com.foxminded.task6.formula1.Formula1;
import ua.com.foxminded.task6.formula1.IncorrectFileContentException;

public class Formula1App {

    public static void main(String[] args) throws IOException{
        Formula1 formula1 = new Formula1();
        try {
            System.out.println(formula1.makeTableResult("abbreviations-2.txt", "start-1.log", "end-2.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IncorrectFileContentException e) {
            e.printStackTrace();
        }
    }
}
