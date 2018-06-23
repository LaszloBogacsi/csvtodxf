package com.csvtodxf.file;

public class CsvLine {

    private String lineElement;
    private String lineElement1;
    private String lineElement2;
    private String lineElement3;
    private String lineElement4;
    private int length;


    public int getLength() {
        return length;
    }

    public String getLineElement() {
        return lineElement;
    }

    public void setLineElement(String lineElement) {
        this.lineElement = lineElement;
    }

    public String getLineElement1() {
        return lineElement1;
    }

    public void setLineElement1(String lineElement1) {
        this.lineElement1 = lineElement1;
    }

    public String getLineElement2() {
        return lineElement2;
    }

    public void setLineElement2(String lineElement2) {
        this.lineElement2 = lineElement2;
    }

    public String getLineElement3() {
        return lineElement3;
    }

    public void setLineElement3(String lineElement3) {
        this.lineElement3 = lineElement3;
    }

    public String getLineElement4() {
        return lineElement4;
    }

    public void setLineElement4(String lineElement4) {
        this.lineElement4 = lineElement4;
    }

    // to use in case of incorrect separator
    public CsvLine(String lineElement) {
        this(lineElement, "", "", "", "", 1);
    }

    public CsvLine(String lineElement, String lineElement1, String lineElement2) {
        this(lineElement, lineElement1, lineElement2, "", "", 3);
    }

    public CsvLine(String lineElement, String lineElement1, String lineElement2, String lineElement3) {
        this(lineElement, lineElement1, lineElement2, lineElement3, "", 4);

    }

    public CsvLine(String lineElement, String lineElement1, String lineElement2, String lineElement3, String lineElement4) {
        this(lineElement, lineElement1, lineElement2, lineElement3, lineElement4, 5);
    }

    private CsvLine(String lineElement, String lineElement1, String lineElement2, String lineElement3, String lineElement4, int length) {
        this.lineElement = lineElement;
        this.lineElement1 = lineElement1;
        this.lineElement2 = lineElement2;
        this.lineElement3 = lineElement3;
        this.lineElement4 = lineElement4;
        this.length = length;
    }
}
