package ua.com.foxminded.task6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ua.com.foxminded.task6.formula1.Formula1;
import ua.com.foxminded.task6.formula1.IncorrectFileContentException;

public class Formula1App {

    public static void main(String[] args) throws IOException {
        Formula1App app = new Formula1App();
        Formula1 formula1 = new Formula1();
        
        Path start = app.getPath("start-1.log");
        Path end = app.getPath("end-2.log");
        Path abbreviations = app.getPath("abbreviations-2.txt");
        
        try {
            System.out.println(formula1.makeTableResult(abbreviations,start,end)); 
        }catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());            
        }catch(IncorrectFileContentException e) {
            e.printStackTrace();
        }
    }

    private Path getPath(String file) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();

        try {
            classLoader.getResource(file).getFile();
        } catch (NullPointerException e) {
            throw new FileNotFoundException("File " + file +" not found");
        }

        File resource = new File(classLoader.getResource(file).getFile());
        Path resourcePath = Paths.get(resource.getAbsolutePath());
        return resourcePath;

    }
}
