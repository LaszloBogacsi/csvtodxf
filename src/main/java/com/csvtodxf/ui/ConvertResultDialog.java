package com.csvtodxf.ui;

import com.csvtodxf.ConversionReport;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class ConvertResultDialog {
    private ConversionReport report;
@FXML private GridPane mainGrid;
    public ConvertResultDialog(ConversionReport report) {
        this.report = report;
    }
}
