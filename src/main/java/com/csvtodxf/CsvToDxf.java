package com.csvtodxf;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvToDxf implements Converter {

    private DrawingConfig config;

    public CsvToDxf() { }

    public CsvToDxf(DrawingConfig config) {
        this.config = config;
    }

    @Override
    public void convert(DrawingConfig config) {
        long start = System.currentTimeMillis();
        this.config = config;
        String dxf = new DXF(config).createDxf(readLines());
        try {
            saveToFile(dxf);
            long duration = System.currentTimeMillis() - start;
            System.out.println("config = " + config);
            System.out.println("duration = " + duration + " ms");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public List<String> readLines() {
        List<String> lines = new ArrayList<>();
        try(Stream<String> stream = Files.lines(this.config.getInputPath())) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("number of lines = " + lines.size());
        return lines;
    }

    public boolean saveToFile(String dxf) throws IOException {
        Files.write(this.config.getOutputPath(), dxf.getBytes());
        return true;
    }
}
