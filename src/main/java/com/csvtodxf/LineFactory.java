package com.csvtodxf;

import com.csvtodxf.file.CsvLine;

import java.util.Arrays;

public class LineFactory {

    public CsvLine createLine(String line, String separator){
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
                return new CsvLine(lineElements[0]); // throw exception
        }
    }
}
