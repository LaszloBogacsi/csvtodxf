package com.csvtodxf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvToDxf implements Converter {

    private DrawingConfig config;
    private ConversionReport report;

    public CsvToDxf(ConversionReport conversionReport) {
        this.report = conversionReport;
    }

    @Override
    public void convert(DrawingConfig config) throws IOException {
        long start = System.currentTimeMillis();
        this.config = config;
        String dxf = new DXF(config).createDxf(readLines());
        saveToFile(dxf);
        long duration = System.currentTimeMillis() - start;
        report.setDurationInMillies(duration);
    }

    private List<String> readLines() throws IOException {
        List<String> lines = Files.lines(this.config.getInputPath()).collect(Collectors.toList());
        report.setNumberOfLinesConverted(lines.size()); // TODO: count the converted lines instead after conversion in the converter
        return lines;
    }

    private boolean saveToFile(String dxf) throws IOException {
        Path newFile = Files.write(this.config.getOutputPath(), dxf.getBytes());
        File file = new File(String.valueOf(newFile));
        report.setFileSize(file.length());
        return true;
    }
}
