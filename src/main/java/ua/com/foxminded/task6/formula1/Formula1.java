package ua.com.foxminded.task6.formula1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream; 

public class Formula1 {
    private static final String TAB = "%-3s %-20s| %-30s | %s %n";
    private static final String LINE_SEPARATOR = "-------------------------------------------------------------------\n";
    
    public String makeTableResult(Path abbreviations, Path start, Path end) throws IOException{
        int counter = 1;
        checkFile(abbreviations);
        Stream<String> abbreviationsStream = Files.lines(abbreviations);
        Map<String, String> startTime = timeTable(start);
        Map<String, String> endTime = timeTable(end);
        String line;
        StringBuilder resultTable = new StringBuilder();
        
        List<Abbreviations> abbreviationList = abbreviationsStream.map(Abbreviations::new)
                .collect(Collectors.toList());
        abbreviationsStream.close();

        Map<String, String> lapTime = new HashMap<>();
        
        startTime
        .forEach((abbreviation, time) -> lapTime.put(abbreviation, calculateLapTime(time, endTime.get(abbreviation))));
        
        abbreviationList
        .stream()
        .forEach(abbreviationsObj -> abbreviationsObj.setTime(lapTime.get(abbreviationsObj.getAbbreviation())));

        List<Abbreviations> sortedByTime = abbreviationList
                .stream()
                .sorted((abbreviationsObj1, abbreviationsObj2) -> abbreviationsObj1.getTime().compareTo(abbreviationsObj2.getTime()))
                .collect(Collectors.toList());
        
        for (Abbreviations abbreviation : sortedByTime) {
            line = String.format(TAB, counter++ + ".", abbreviation.getRacer(), abbreviation.getCar(), abbreviation.getTime());
            resultTable.append(line);
            if (counter == 16)
                resultTable.append(LINE_SEPARATOR);
        }
        return resultTable.toString();
    }

    private String calculateLapTime(String start, String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        Duration duration = Duration.between(startTime, endTime);
        String lapTime = LocalTime.ofNanoOfDay(duration.toNanos()).toString();
        return lapTime.substring(4);
    }

    private Map<String, String> timeTable(Path path) throws IOException {
        Stream<String> fileStream = Files.lines(path);
        Map<String, String> timeMap = fileStream
                .collect(Collectors.toMap(abbreviation -> abbreviation.substring(0, 3), time -> time.substring(3, time.length()).replace('_', 'T')));
        fileStream.close();
        return timeMap;
    }
    
    private void checkFile(Path file) throws IOException {
        Pattern p = Pattern.compile("^[A-Z]{3}_[A-Za-z]+\\s[A-Za-z]+_[A-Z\\s]+$");
        Files.lines(file).map(p::matcher).filter(Matcher::matches).forEach(System.out::println);
       // Files.lines(file).noneMatch();
               
    }
}
