package ua.com.foxminded.task6.formula1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formula1 {
    public void makeTable(Path abbreviations, Path start, Path end) throws IOException {
        int num =1;
                
        Stream<String> abbreviationsStream = Files.lines(abbreviations);
        List<Abbreviations> abbreviationList = abbreviationsStream
                .map(abbreviation -> new Abbreviations(abbreviation))
                .collect(Collectors.toList());

        Map<String, String> mapOfStart = timeTable(start);
        Map<String, String> mapOfEnd = timeTable(end);
        Map<String, String> lapTime = new HashMap();
        mapOfStart.forEach((k, v) -> lapTime.put(k, calculateLapTime(v, mapOfEnd.get(k))));
        abbreviationList
        .stream()
        .forEach(ab -> ab.setTime(lapTime.get(ab.getAbbreviation())));
        
        List<Abbreviations> abbreviationsSorted = abbreviationList
                .stream()
                .sorted((o1,o2) -> o1.getTime().compareTo(o2.getTime()))
                .collect(Collectors.toList());
        for (Abbreviations abbreviations2 : abbreviationsSorted) {
            System.out.printf("%-3s %-20s| %-30s | %s %n", num++ + ".", abbreviations2.getRacer(), abbreviations2.getCar(), abbreviations2.getTime());
            if(num==16)
                System.out.println("-------------------------------------------------------------------");
        }        
    }
    private String calculateLapTime(String start, String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        Duration duration = Duration.between(startTime, endTime);
        String timeLap = LocalTime.ofNanoOfDay(duration.toNanos()).toString();
        return timeLap.substring(4);
    }

    private Map<String, String> timeTable(Path path) throws IOException{
        Stream<String> abb = Files.lines(path);
        Map<String, String> mapSt = abb
                .collect(Collectors.toMap(x -> x.toString().substring(0, 3), x -> x.toString().substring(3, x.length()).replace('_', 'T')));
        return  mapSt;
    }
}
