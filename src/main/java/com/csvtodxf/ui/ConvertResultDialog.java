package com.csvtodxf.ui;

import com.csvtodxf.ConversionReport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConvertResultDialog extends Dialog {
    @FXML
    private Label numberOfLinesLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label fileSizeLabel;
    @FXML
    private Button okButton;
    private Stage dialogStage;

    @FXML
    void initialize() {
    }

    @FXML
    public void okPressed() {
        this.dialogStage.close();
    }

    public void setReport(ConversionReport report) {
        this.numberOfLinesLabel.setText(String.valueOf(report.getNumberOfLinesConverted()));
        this.durationLabel.setText(String.valueOf(report.getDurationInMillies()) + " ms");
        this.fileSizeLabel.setText(String.valueOf(Math.round(report.getFileSize() / 1024 * 100) / 100.0) + " kb");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
