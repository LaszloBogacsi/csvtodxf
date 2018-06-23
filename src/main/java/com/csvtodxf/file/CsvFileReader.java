package com.csvtodxf.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvFileReader implements FileReader {


    @Override
    public List<CsvLine> readLine(Path path, String separator) {
        List<CsvLine> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.filter(line -> line.trim().length() != 0).map(line -> createLine(line, separator)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public List<CsvLine> readBeginning(Path path, int limit, final String separator) {
        List<CsvLine> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.limit(limit).filter(line -> line.trim().length() != 0).map(line -> createLine(line, separator)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private CsvLine createLine(String line, String separator) {
        // trim whitespace
        String[] lineElements = Arrays.stream(line.split(separator)).map(String::trim).toArray(String[]::new);
        // min 3 max 5 args
        switch (lineElements.length) {
            case 3:
                return new CsvLine(lineElements[0], lineElements[1], lineElements[2]);
            case 4:
                return new CsvLine(lineElements[0], lineElements[1], lineElements[2], lineElements[3]);
            case 5:
                return new CsvLine(lineElements[0], lineElements[1], lineElements[2], lineElements[3], lineElements[4]);
            default:
                return new CsvLine(lineElements[0]);
        }
    }
}
