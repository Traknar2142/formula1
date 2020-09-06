package ua.com.foxminded.task6.formula1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formula1 {
    private static final String TAB = "%-3s %-20s| %-30s | %s %n";
    private static final String LINE_SEPARATOR = "-------------------------------------------------------------------\n";
    private static final String PATTERN_ABBREVIATIONS = "^[A-Z]{3}_[A-Za-z]+\\s[A-Za-z]+_[A-Z\\s]+$";
    private static final String PATTERN_TIMELAP = "^[A-Z]{3}2018-05-24_[01][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{3}$";

    public String makeTableResult(String abbreviations, String startTime, String endTime) throws IOException, IncorrectFileContentException {
        Path abbreviationsPath = getPath(abbreviations);
        Path startTimePath = getPath(startTime);
        Path endTimePath = getPath(endTime);
        
        checkFileContent(abbreviationsPath, PATTERN_ABBREVIATIONS);
        checkFileContent(startTimePath,PATTERN_TIMELAP);
        checkFileContent(endTimePath, PATTERN_TIMELAP);

        Stream<String> abbreviationsStream = Files.lines(abbreviationsPath);
        Map<String, String> startTimes = getLapInfo(startTimePath);
        Map<String, String> endTimes = getLapInfo(endTimePath);
        
        List<Abbreviations> abbreviationList = abbreviationsStream
                .map(Abbreviations::new)
                .collect(Collectors.toList());
        abbreviationsStream.close();  
        
        List<Abbreviations> sortedByTimeLap = getRaceInfo(abbreviationList,startTimes,endTimes);
        
        return makeTableOfRacers(sortedByTimeLap);        
    }
    
    private List<Abbreviations> getRaceInfo(List<Abbreviations> abbreviations, Map<String, String> startTimes, Map<String, String> endTimes) {
        Map<String, String> lapTime = new HashMap<>();

        startTimes.forEach((abbreviation, time) -> lapTime.put(abbreviation, calculateLapTime(time, endTimes.get(abbreviation))));

        abbreviations.stream()
                .forEach(abbreviationsObj -> abbreviationsObj.setTime(lapTime.get(abbreviationsObj.getAbbreviation())));

        List<Abbreviations> sortedByTime = abbreviations
                .stream().sorted((abbreviationsObj1, abbreviationsObj2) -> abbreviationsObj1.getTime()
                        .compareTo(abbreviationsObj2.getTime()))
                .collect(Collectors.toList());
        return sortedByTime;
    }

    private String makeTableOfRacers(List<Abbreviations> raceInfo) {
        int counter = 1;
        int firstLooserRacer = 16;
        StringBuilder resultTable = new StringBuilder();

        for (Abbreviations abbreviation : raceInfo) {
            String line = String.format(TAB, counter++ + ".", abbreviation.getRacer(), abbreviation.getCar(),
                    abbreviation.getTime());
            resultTable.append(line);
            if (counter == firstLooserRacer)
                resultTable.append(LINE_SEPARATOR);
        }
        return resultTable.toString();
    }

    private String calculateLapTime(String start, String end) {
        int hours = 4;
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        Duration duration = Duration.between(startTime, endTime);
        String lapTime = LocalTime.ofNanoOfDay(duration.toNanos()).toString();
        return lapTime.substring(hours);
    }

    private Map<String, String> getLapInfo(Path path) throws IOException {
        Stream<String> fileStream = Files.lines(path);
        Map<String, String> timeMap = fileStream.collect(Collectors.toMap(abbreviation -> abbreviation.substring(0, 3), time -> time.substring(3, time.length()).replace('_', 'T')));
        fileStream.close();
        return timeMap;
    }

    private void checkFileContent(Path file, String regex) throws IOException, IncorrectFileContentException {
        Pattern pattern = Pattern.compile(regex);
        List<String> checkList = new ArrayList<>();
        Stream<String> fileStream =  Files.lines(file);
        
        fileStream.map(string -> pattern.matcher(string)).forEach(match -> {
            if (!match.matches()) {
                checkList.add(match.replaceAll(""));
            }
        });
        fileStream.close();
        if (!checkList.isEmpty()) {
            checkList.stream().forEach(line -> System.err.println("Error in line " + (checkList.indexOf(line)+1) +": " + line));
            throw new IncorrectFileContentException("Please, check the file: " + file.getFileName());
        }
    }
    
    private Path getPath(String file) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        
        if (classLoader.getResource(file) == null) {
            throw new FileNotFoundException("File " + file + " not found");
        }
        
        File resource = new File(classLoader.getResource(file).getFile());
        Path resourcePath = Paths.get(resource.getAbsolutePath());
        return resourcePath;
    }
}
