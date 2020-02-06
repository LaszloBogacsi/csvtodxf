package com.csvtodxf.file;

import java.util.Optional;

public class CsvLinePreview {

    private final String lineElement;
    private final String lineElement1;
    private final String lineElement2;
    private final String lineElement3;
    private final String lineElement4;

    public CsvLinePreview(String lineElement, String lineElement1, String lineElement2, String lineElement3, String lineElement4) {

        this.lineElement = lineElement;
        this.lineElement1 = lineElement1;
        this.lineElement2 = lineElement2;
        this.lineElement3 = lineElement3;
        this.lineElement4 = lineElement4;
    }

    public String getLineElement() {
        return lineElement;
    }

    public String getLineElement1() {
        return lineElement1;
    }

    public String getLineElement2() {
        return lineElement2;
    }

    public String getLineElement3() {
        return lineElement3;
    }

    public String getLineElement4() {
        return lineElement4;
    }
}
