package com.csvtodxf.file;

import com.csvtodxf.LineFactory;

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

    private LineFactory lineFactory;

    public CsvFileReader(LineFactory lineFactory) {
        this.lineFactory = lineFactory;
    }

    @Override
    public List<CsvLine> readLine(Path path, final String separator) {
        List<CsvLine> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.filter(line -> line.trim().length() != 0)
                    .map(line -> lineFactory.createLine(line, separator))
                    .filter(csvLine -> !csvLine.getLineElement1().isEmpty() && !csvLine.getLineElement2().isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    // For Preview only, doesn't filter out the invalid line lengths
    @Override
    public List<CsvLine> readLinePreview(Path path, final String separator, int limit) {
        List<CsvLine> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.limit(limit).filter(line -> line.trim().length() != 0)
                    .map(line -> lineFactory.createLine(line, separator))
            .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
