package com.csvtodxf;

import com.csvtodxf.file.CsvLine;
import com.csvtodxf.file.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvToDxf implements Converter {

    private DrawingConfig config;
    private FileReader reader;
    private ConversionReport report;

    public CsvToDxf(FileReader reader, ConversionReport conversionReport) {
        this.reader = reader;
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

    private List<CsvLine> readLines() throws IOException {
        List<CsvLine> lines = reader.readLine(this.config.getInputPath(), config.getSeparator());
        report.setNumberOfLinesConverted(lines.size()); // TODO: count the converted lines instead after conversion in the converter
        return lines;
    }

    private void saveToFile(String dxf) throws IOException {
        Path newFile = Files.write(this.config.getOutputPath(), dxf.getBytes());
        File file = new File(String.valueOf(newFile));
        report.setFileSize(file.length());
    }
}
