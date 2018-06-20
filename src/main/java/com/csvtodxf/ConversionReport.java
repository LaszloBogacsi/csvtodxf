package com.csvtodxf;

public class ConversionReport {
    private int numberOfLinesConverted;
    private long durationInMillies;
    private double fileSize;

    public ConversionReport () { }
    public ConversionReport(int numberOfLinesConverted, long durationInMillies, double fileSize) {
        this.numberOfLinesConverted = numberOfLinesConverted;
        this.durationInMillies = durationInMillies;
        this.fileSize = fileSize;
    }

    public long getNumberOfLinesConverted() {
        return numberOfLinesConverted;
    }

    public void setNumberOfLinesConverted(int numberOfLinesConverted) {
        this.numberOfLinesConverted = numberOfLinesConverted;
    }

    public long getDurationInMillies() {
        return durationInMillies;
    }

    public void setDurationInMillies(long durationInMillies) {
        this.durationInMillies = durationInMillies;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "ConversionReport{" +
                "numberOfLinesConverted=" + numberOfLinesConverted +
                ", durationInMillies=" + durationInMillies +
                ", fileSize=" + fileSize +
                '}';
    }
}
