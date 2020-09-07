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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formula1 {
    private static final String TAB = "%-3s %-20s| %-30s | %s %n";
    private static final String LINE_SEPARATOR = "-------------------------------------------------------------------\n";
    private static final String PATTERN_ABBREVIATIONS = "^[A-Z]{3}_[A-Za-z]+\\s[A-Za-z]+_[A-Z\\s]+$";
    private static final String PATTERN_TIMELAP = "^[A-Z]{3}2018-05-24_[01][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{3}$";

    public String makeTableResult(String abbreviationsInfo, String startTimeLog, String endTimeLog) throws IOException, IncorrectFileContentException {
        Path abbreviationsPath = getPath(abbreviationsInfo);
        Path startTimePath = getPath(startTimeLog);
        Path endTimePath = getPath(endTimeLog);

        checkFileContent(abbreviationsPath, PATTERN_ABBREVIATIONS);
        checkFileContent(startTimePath, PATTERN_TIMELAP);
        checkFileContent(endTimePath, PATTERN_TIMELAP);
        List<Abbreviations> abbreviationList = new ArrayList<>();

        try (Stream<String> abbreviationsStream = Files.lines(abbreviationsPath)) {
            abbreviationList = abbreviationsStream
                    .map(Abbreviations::new)
                    .collect(Collectors.toList());
        }

        Map<String, String> startTimes = getLapInfo(startTimePath);
        Map<String, String> endTimes = getLapInfo(endTimePath);
        List<Abbreviations> sortedByTimeLap = getRaceInfo(abbreviationList, startTimes, endTimes);

        return makeTableOfRacers(sortedByTimeLap);
    }    

    private String makeTableOfRacers(List<Abbreviations> raceInfo) {
        int counter = 1;
        int firstLooserRacer = 16;
        StringBuilder resultTable = new StringBuilder();

        for (Abbreviations abbreviation : raceInfo) {
            String line = String.format(TAB, counter++ + ".", abbreviation.getRacer(), abbreviation.getCar(), abbreviation.getTime());
            resultTable.append(line);
            if (counter == firstLooserRacer)
                resultTable.append(LINE_SEPARATOR);
        }
        return resultTable.toString();
    }

    private List<Abbreviations> getRaceInfo(List<Abbreviations> abbreviations, Map<String, String> startTimes, Map<String, String> endTimes) {
        Map<String, String> lapTime = new HashMap<>();

        startTimes.forEach((abbreviation, time) -> lapTime.put(abbreviation, calculateLapTime(time, endTimes.get(abbreviation))));

        abbreviations
                .stream()
                .forEach(abbreviationsObj -> abbreviationsObj.setTime(lapTime.get(abbreviationsObj.getAbbreviation())));

        List<Abbreviations> sortedByTime = abbreviations
                .stream()
                .sorted((abbreviationsObj1, abbreviationsObj2) -> abbreviationsObj1.getTime().compareTo(abbreviationsObj2.getTime()))
                .collect(Collectors.toList());
        return sortedByTime;
    }

    private String calculateLapTime(String start, String end) {
        int unnecessaryHours = 4;
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        Duration duration = Duration.between(startTime, endTime);
        String lapTime = LocalTime.ofNanoOfDay(duration.toNanos()).toString();
        
        return lapTime.substring(unnecessaryHours);
    }

    private Map<String, String> getLapInfo(Path path) throws IOException {
        int firstLetterAbbriviation = 0;
        int lastLetterAbbriviation = 3;
        Map<String, String> lapInfo = new HashMap<>();
        try (Stream<String> fileStream = Files.lines(path)) {
            lapInfo = fileStream.collect(Collectors.toMap(
                    abbreviation -> abbreviation.substring(firstLetterAbbriviation, lastLetterAbbriviation),
                    time -> time.substring(lastLetterAbbriviation, time.length()).replace('_', 'T')));
        }
        
        return lapInfo;
    }

    private void checkFileContent(Path file, String regex) throws IOException, IncorrectFileContentException {
        List<String> incorrectLines = new ArrayList<>();
        try (Stream<String> fileStream = Files.lines(file)) {
            incorrectLines = fileStream
                    .filter(string -> !string.matches(regex))
                    .collect(Collectors.toList());
        }
        if (!incorrectLines.isEmpty()) {
            throw new IncorrectFileContentException("Incorrect lines occur in file: " + file.getFileName() + "\n" + getIncorrectLines(incorrectLines));
        }
    }

    private String getIncorrectLines(List<String> incorrectLines) {
        StringBuilder result = new StringBuilder("Incorrect lines:\n");
        incorrectLines.stream().forEach(line -> result.append(line + "\n"));

        return result.toString().trim();
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
