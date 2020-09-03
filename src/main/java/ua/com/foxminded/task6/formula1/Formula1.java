package ua.com.foxminded.task6.formula1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formula1 {
    private static final String TAB = "%-3s %-20s| %-30s | %s %n";
    private static final String LINE_SEPARATOR = "-------------------------------------------------------------------\n";
    private static final String PATTERN_ABBREVIATIONS = "^[A-Z]{3}_[A-Za-z]+\\s[A-Za-z]+_[A-Z\\s]+$";
    private static final String PATTERN_TIMELAP = "^[A-Z]{3}2018-05-24_[01][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{3}$";

    public String makeTableResult(Path abbreviations, Path start, Path end) throws IOException, IncorrectFileContentException {
        int counter = 1;
        int firstLooserRacer = 16;
        
        checkFileContentAbbrevstions(abbreviations);
        checkFileContentLaptime(start);
        checkFileContentLaptime(end);

        Stream<String> abbreviationsStream = Files.lines(abbreviations);
        Map<String, String> startTime = getLapInfo(start);
        Map<String, String> endTime = getLapInfo(end);
        StringBuilder resultTable = new StringBuilder();

        List<Abbreviations> abbreviationList = abbreviationsStream
                .map(Abbreviations::new)
                .collect(Collectors.toList());
        abbreviationsStream.close();

        Map<String, String> lapTime = new HashMap<>();

        startTime.forEach((abbreviation, time) -> lapTime.put(abbreviation, calculateLapTime(time, endTime.get(abbreviation))));

        abbreviationList.stream()
                .forEach(abbreviationsObj -> abbreviationsObj.setTime(lapTime.get(abbreviationsObj.getAbbreviation())));

        List<Abbreviations> sortedByTime = abbreviationList
                .stream().sorted((abbreviationsObj1, abbreviationsObj2) -> abbreviationsObj1.getTime().compareTo(abbreviationsObj2.getTime()))
                .collect(Collectors.toList());

        for (Abbreviations abbreviation : sortedByTime) {
            String line = String.format(TAB, counter++ + ".", abbreviation.getRacer(), abbreviation.getCar(), abbreviation.getTime());
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

    private void checkFileContentAbbrevstions(Path file) throws IOException, IncorrectFileContentException {
        Pattern pattern = Pattern.compile(PATTERN_ABBREVIATIONS);
        List<Boolean> checkList = new ArrayList<>();
        Files.lines(file).map(string -> pattern.matcher(string)).forEach(match -> checkList.add(match.matches()));
        if (checkList.contains(false)) {
            System.err.println("Error in " + (checkList.indexOf(false) + 1) + " line in file - " + file);
            throw new IncorrectFileContentException("Please, check the file: " + file.getFileName());
        }
    }

    private void checkFileContentLaptime(Path file) throws IOException, IncorrectFileContentException {
        Pattern pattern = Pattern.compile(PATTERN_TIMELAP);
        List<Boolean> checkList = new ArrayList<>();
        Files.lines(file).map(string -> pattern.matcher(string)).forEach(match -> checkList.add(match.matches()));
        if (checkList.contains(false)) {
            System.err.println("Error in " + (checkList.indexOf(false) + 1) + " string in file - " + file);
            throw new IncorrectFileContentException("Please, check the file: " + file.getFileName());
        }
    }
}
