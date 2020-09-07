package ua.com.foxminded.task6.formula1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import ua.com.foxminded.task6.Formula1App;

class Formula1Test {
    Formula1 formula1 = new Formula1();
    
    @Test
    void makeTableResult_ShoudReturnResultTableWithSeparateLine_WhenInputIsCorrect() throws IOException, IncorrectFileContentException {
        String expected = "1.  Sebastian Vettel    | FERRARI                        | 1:04.415 \r\n"
                        + "2.  Daniel Ricciardo    | RED BULL RACING TAG HEUER      | 1:12.013 \r\n"
                        + "3.  Valtteri Bottas     | MERCEDES                       | 1:12.434 \r\n"
                        + "4.  Lewis Hamilton      | MERCEDES                       | 1:12.460 \r\n"
                        + "5.  Stoffel Vandoorne   | MCLAREN RENAULT                | 1:12.463 \r\n"
                        + "6.  Kimi Raikkonen      | FERRARI                        | 1:12.639 \r\n"
                        + "7.  Fernando Alonso     | MCLAREN RENAULT                | 1:12.657 \r\n"
                        + "8.  Sergey Sirotkin     | WILLIAMS MERCEDES              | 1:12.706 \r\n"
                        + "9.  Charles Leclerc     | SAUBER FERRARI                 | 1:12.829 \r\n"
                        + "10. Sergio Perez        | FORCE INDIA MERCEDES           | 1:12.848 \r\n"
                        + "11. Romain Grosjean     | HAAS FERRARI                   | 1:12.930 \r\n"
                        + "12. Pierre Gasly        | SCUDERIA TORO ROSSO HONDA      | 1:12.941 \r\n"
                        + "13. Carlos Sainz        | RENAULT                        | 1:12.950 \r\n"
                        + "14. Esteban Ocon        | FORCE INDIA MERCEDES           | 1:13.028 \r\n"
                        + "15. Nico Hulkenberg     | RENAULT                        | 1:13.065 \r\n"
                        + "-------------------------------------------------------------------\n"
                        + "16. Brendon Hartley     | SCUDERIA TORO ROSSO HONDA      | 1:13.179 \r\n"
                        + "17. Marcus Ericsson     | SAUBER FERRARI                 | 1:13.265 \r\n"
                        + "18. Lance Stroll        | WILLIAMS MERCEDES              | 1:13.323 \r\n"
                        + "19. Kevin Magnussen     | HAAS FERRARI                   | 1:13.393 \r\n";
        assertEquals(expected, formula1.makeTableResult("abbreviations.txt", "startLapTime.log", "endLapTime.log"));
    }

    @Test
    void makeTableResult_ShoudReturnResultTableWithoutSeparateLine_WhenInputIsCorrect() throws IOException, IncorrectFileContentException {
        String expected = "1.  Sebastian Vettel    | FERRARI                        | 1:04.415 \r\n"
                        + "2.  Daniel Ricciardo    | RED BULL RACING TAG HEUER      | 1:12.013 \r\n"
                        + "3.  Valtteri Bottas     | MERCEDES                       | 1:12.434 \r\n"
                        + "4.  Lewis Hamilton      | MERCEDES                       | 1:12.460 \r\n"
                        + "5.  Kimi Raikkonen      | FERRARI                        | 1:12.639 \r\n"
                        + "6.  Fernando Alonso     | MCLAREN RENAULT                | 1:12.657 \r\n"
                        + "7.  Esteban Ocon        | FORCE INDIA MERCEDES           | 1:13.028 \r\n";
        assertEquals(expected, formula1.makeTableResult("abbreviationsSevenRacers.txt", "start-1.log", "end-2.log"));
    }

    @Test
    void makeTableResult_ShoudReturnResultTableWithSingleRacer_WhenInputAbbreviationsIsContainsOneRacer() throws IOException, IncorrectFileContentException {
        String expected = "1.  Daniel Ricciardo    | RED BULL RACING TAG HEUER      | 1:12.013 \r\n";
        assertEquals(expected, formula1.makeTableResult("abbreviationsOneRacer.txt", "startLapTime.log", "endLapTime.log"));
    }

    @Test
    void makeTableResult_ShoudReturnNothing_WhenInputIsAbbreviationsIsEmpty() throws IOException, IncorrectFileContentException {
        String expected = "";
        assertEquals(expected, formula1.makeTableResult("empty.txt", "startLapTime.log", "endLapTime.log"));
    }

    @Test
    void makeTableResult_ShouldThrowIncorrectFileContentException_WhenAbbrevationsFileIncorrect() {
        assertThrows(IncorrectFileContentException.class, () -> {
            formula1.makeTableResult("abbreviations-wrong-content.txt", "startLapTime.log", "endLapTime.log");
        });
    }

    @Test
    void makeTableResult_ShouldThrowIncorrectFileContentException_WhenStartFileIncorrect() {
        assertThrows(IncorrectFileContentException.class, () -> {
            formula1.makeTableResult("abbreviations.txt", "start-wrong-content.log", "endLapTime.log");
        });
    }

    @Test
    void makeTableResult_ShouldThrowIncorrectFileContentException_WhenEndFileIncorrect() {
        assertThrows(IncorrectFileContentException.class, () -> {
            formula1.makeTableResult("abbreviations.txt", "startLapTime.log", "end-wrong-content.log");
        });
    }
    
    @Test
    void getPath_ShouldThrowFileNotFoundException_WhenFileIsMissing() throws FileNotFoundException {
        assertThrows(FileNotFoundException.class, () -> {
            formula1.makeTableResult("missFile","startLapTime.log", "end-wrong-content.log");
        });
    }
}
