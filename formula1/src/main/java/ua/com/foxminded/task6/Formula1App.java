package ua.com.foxminded.task6;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ua.com.foxminded.task6.formula1.Formula1;

public class Formula1App {

    public static void main(String[] args) throws IOException {
        Path start = Paths.get("src/main/resources/start-1.log");
        Path end = Paths.get("src/main/resources/end-2.log");
        Path abbreviations = Paths.get("src/main/resources/abbreviations-2.txt");     
        Formula1 formula1 = new Formula1();
        formula1.makeTable(abbreviations,start,end);
    }
}
