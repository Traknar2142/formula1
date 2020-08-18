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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formula1 {
    public void makeTable(Path abbreviations, Path start, Path end) throws IOException {

                
        Stream<String> abbreviationsStream = Files.lines(abbreviations);
        Set<Abbreviations> abbreviationsSet = abbreviationsStream
                .map(abbreviation -> new Abbreviations(abbreviation))
                .collect(Collectors.toSet());

        Map<String, String> mapOfStart = timeTable(start);
        Map<String, String> mapOfEnd = timeTable(end);
        Map<String,String> lapTime = new HashMap();
        mapOfStart.forEach((k,v) -> lapTime.put(k, calculateLapTime(v, mapOfEnd.get(k))));

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
