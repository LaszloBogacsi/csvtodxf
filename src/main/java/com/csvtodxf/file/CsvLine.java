package com.csvtodxf.file;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CsvLine {

    private String lineElement;
    private String lineElement1;
    private String lineElement2;
    private Optional<String> lineElement3;
    private Optional<String> lineElement4;



    public String getLineElement() {
        return lineElement;
    }

    public String getLineElement1() {
        return lineElement1;
    }

    public String getLineElement2() {
        return lineElement2;
    }

    public Optional<String> getLineElement3() {
        return lineElement3;
    }

    public Optional<String> getLineElement4() {
        return lineElement4;
    }

    // to use in case of incorrect separator
    public CsvLine(String lineElement) {
        this(lineElement, "", "", Optional.empty(), Optional.empty());
    }

    public CsvLine(String lineElement, String lineElement1, String lineElement2) {
        this(lineElement, lineElement1, lineElement2, Optional.empty(), Optional.empty());
    }

    public CsvLine(String lineElement, String lineElement1, String lineElement2, String lineElement3) {
        this(lineElement, lineElement1, lineElement2, Optional.of(lineElement3), Optional.empty());
    }

    public CsvLine(String lineElement, String lineElement1, String lineElement2, String lineElement3, String lineElement4) {
        this(lineElement, lineElement1, lineElement2, Optional.ofNullable(lineElement3.isEmpty() ? null : lineElement3), Optional.ofNullable(lineElement4.isEmpty() ? null : lineElement4));
    }

    private CsvLine(String lineElement, String lineElement1, String lineElement2, Optional<String> lineElement3, Optional<String> lineElement4) {
        this.lineElement = lineElement;
        this.lineElement1 = lineElement1;
        this.lineElement2 = lineElement2;
        this.lineElement3 = lineElement3;
        this.lineElement4 = lineElement4;
    }

}
