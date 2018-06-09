package com.csvtodxf.ui;


import com.csvtodxf.Converter;
import com.csvtodxf.CsvToDxf;
import com.csvtodxf.DrawingConfig;
import com.csvtodxf.file.CsvFileReader;
import com.csvtodxf.file.CsvLine;
import com.csvtodxf.file.FileReader;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;


public class Controller {
    private final int PREVIEW_LIST_LENGTH = 5;
    private DrawingConfig.DrawingConfigBuilder configBuilder = DrawingConfig.builder();
    @FXML private VBox rootVBox;
    @FXML private MenuItem openMenu;
    @FXML private MenuItem saveAsMenu;
    @FXML private MenuItem closeMenu;
    @FXML private MenuItem aboutMenu;
    @FXML private Button inputBrowseButton;
    @FXML private Button outputBrowseButton;
    @FXML private TextField inputTextField;
    @FXML private TextField outputTextField;
    @FXML private TableView previewTable;
    @FXML private TableColumn pointIdCol;
    @FXML private TableColumn eastingCol;
    @FXML private TableColumn northingCol;
    @FXML private TableColumn heightCol;
    @FXML private TableColumn codeCol;
    @FXML private ChoiceBox<String> choiceBoxSep;
    @FXML private TextField textHeightField;
    @FXML private CheckBox pointNumberCheckBox;
    @FXML private CheckBox heightCheckBox;
    @FXML private CheckBox coordinatedCheckBox;
    @FXML private CheckBox codeCheckBox;
    @FXML private ToggleGroup dimensionGroup;
    @FXML private RadioButton is3DButton;

    @FXML private Button convertButton;

    @FXML
    public void initialize() {
        openMenu.setOnAction(e -> browseInput(e));
        saveAsMenu.setOnAction(e -> browseOutput(e));
        closeMenu.setOnAction(e -> System.exit(0));
        aboutMenu.setOnAction(e -> openAbout(e));
        inputBrowseButton.setOnAction(e -> browseInput(e));
        outputBrowseButton.setOnAction(e -> browseOutput(e));
        ObservableList<String> options = FXCollections.observableArrayList(",",";");
        final String DEFAULT_SEP = ",";
        final String DEFAULT_TEXT_HEIGHT = "1.0";
        configBuilder.setSeparator(DEFAULT_SEP);
        choiceBoxSep.setValue(DEFAULT_SEP);
        choiceBoxSep.setItems(options);
        choiceBoxSep.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    configBuilder.setSeparator(newValue);
                    String absInputFilePath = inputTextField.getText();
                    if(!absInputFilePath.isEmpty()) {
                        reloadPreview(absInputFilePath, newValue);
                    }
                });
        textHeightField.setText(DEFAULT_TEXT_HEIGHT);
        dimensionGroup = new ToggleGroup();
        convertButton.setOnAction(e -> convert(e));

    }

    private void openAbout(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About CSV to DXF");
        alert.setHeaderText(null);
        alert.setContentText("This app is free to use\nby Laszlo Bogacsi\n@2018");

        alert.showAndWait();
    }

    private void browseInput(ActionEvent e) {
        Stage stage = (Stage) rootVBox.getScene().getWindow();
        FileChooser inputFilechooser = new FileChooser();
        // show only csv files
        FileChooser.ExtensionFilter csvExtFilter =  new FileChooser.ExtensionFilter("*.csv, *.txt files", "*.txt, *.csv");
        inputFilechooser.getExtensionFilters().add(csvExtFilter);
        File selectedFile = inputFilechooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String absInputFilePath = selectedFile.getPath();
            inputTextField.setText(absInputFilePath);
            configBuilder.setInputPath(Paths.get(absInputFilePath));
            reloadPreview(absInputFilePath, choiceBoxSep.getValue());
        }


    }

    private void browseOutput(ActionEvent e) {
        Stage stage = (Stage) rootVBox.getScene().getWindow();
        FileChooser outputFilechooser = new FileChooser();
        // save only dxf files
        FileChooser.ExtensionFilter csvExtFilter =  new FileChooser.ExtensionFilter("*.dxf files", "*.dxf");
        outputFilechooser.getExtensionFilters().add(csvExtFilter);
        File selectedFile = outputFilechooser.showSaveDialog(stage);
        if (selectedFile != null) {
            String absInputFilePath = selectedFile.getPath();
            outputTextField.setText(absInputFilePath);
            configBuilder.setOutputPath(Paths.get(absInputFilePath));
        }
    }


    private void convert(ActionEvent e) {
        configBuilder.setTextHeight(parseStringInputToDouble(textHeightField.getText()));
        configBuilder.setDoPrintId(pointNumberCheckBox.isSelected());
        configBuilder.setDoPrintHeight(heightCheckBox.isSelected());
        configBuilder.setDoPrintCoords(coordinatedCheckBox.isSelected());
        configBuilder.setDoPrintCode(codeCheckBox.isSelected());
        configBuilder.setIs3D(is3DButton.isSelected());

        Converter converter = new CsvToDxf();
        converter.convert(configBuilder.build());
    }

    /**
     * Creates a read only table to preview the first n lines of the input com.csvtodxf.file
     * @param path to the input com.csvtodxf.file, String, absolute path
     */
    private void reloadPreview(String path, String separator) {
        // clear table before reload
        previewTable.getItems().clear();
        FileReader reader = new CsvFileReader();
        List<CsvLine> previewLines = reader.readBeginning(path, PREVIEW_LIST_LENGTH, separator);
        // map columns to data properties in CSV lines type
        pointIdCol.setCellValueFactory(new PropertyValueFactory<CsvLine, String>("lineElement"));
        eastingCol.setCellValueFactory(new PropertyValueFactory<CsvLine, String>("lineElement1"));
        northingCol.setCellValueFactory(new PropertyValueFactory<CsvLine, String>("lineElement2"));
        heightCol.setCellValueFactory(new PropertyValueFactory<CsvLine, String>("lineElement3"));
        codeCol.setCellValueFactory(new PropertyValueFactory<CsvLine, String>("lineElement4"));
        // add all items to the table
        previewTable.getItems().addAll(previewLines);
    }

    private double parseStringInputToDouble(String input) {
        // TODO: handle numberformat exception in case of incorrect input format
        return Double.parseDouble(input.replaceAll(",",".").replaceAll("[^0-9.]+", ""));
    }

    // TODO: if user changes the com.csvtodxf.file path in the text field then set a listener to followup the change and reload the table with sampledata.






}