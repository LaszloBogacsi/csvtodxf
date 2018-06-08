package com.csvtodxf.ui;

public class InputFormatException extends Throwable {
    public InputFormatException(NumberFormatException message) {
        super(message);
    }
}
