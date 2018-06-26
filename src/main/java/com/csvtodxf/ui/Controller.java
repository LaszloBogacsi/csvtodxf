package com.csvtodxf.ui;


import com.csvtodxf.*;
import com.csvtodxf.file.CsvFileReader;
import com.csvtodxf.file.CsvLine;
import com.csvtodxf.file.FileReader;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    private final String DEFAULT_SEP = ",";
    private final String DEFAULT_TEXT_HEIGHT = "1.0";
    private Main main;
    FileReader reader = new CsvFileReader();


    public String getAbsInputFilePath() {
        return absInputFilePath;
    }

    public void setAbsInputFilePath(String absInputFilePath) {
        this.absInputFilePath = absInputFilePath;
    }

    private String absInputFilePath;
    @FXML
    private VBox rootVBox;
    @FXML
    private MenuItem openMenu;
    @FXML
    private MenuItem saveAsMenu;
    @FXML
    private MenuItem closeMenu;
    @FXML
    private MenuItem aboutMenu;
    @FXML
    private Button inputBrowseButton;
    @FXML
    private Button outputBrowseButton;
    @FXML
    private TextField inputTextField;
    @FXML
    private TextField outputTextField;
    @FXML
    private TableView previewTable;
    @FXML
    private TableColumn pointIdCol;
    @FXML
    private TableColumn eastingCol;
    @FXML
    private TableColumn northingCol;
    @FXML
    private TableColumn heightCol;
    @FXML
    private TableColumn codeCol;
    @FXML
    private ChoiceBox<String> choiceBoxSep;
    @FXML
    private TextField textHeightField;
    @FXML
    private CheckBox pointNumberCheckBox;
    @FXML
    private CheckBox heightCheckBox;
    @FXML
    private CheckBox coordinatedCheckBox;
    @FXML
    private CheckBox codeCheckBox;
    @FXML
    private CheckBox layersByCodeCheckbox;
    @FXML
    private ToggleGroup dimensionGroup;
    @FXML
    private RadioButton is3DButton;
    @FXML
    private Button convertButton;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {
        openMenu.setOnAction(this::browseInput);
        saveAsMenu.setOnAction(this::browseOutput);
        closeMenu.setOnAction(e -> System.exit(0));
        aboutMenu.setOnAction(this::openAbout);
        inputBrowseButton.setOnAction(this::browseInput);
        outputBrowseButton.setOnAction(this::browseOutput);
        previewTable.setPlaceholder(new Label("Open an input file for content to appear"));
        ObservableList<String> options = FXCollections.observableArrayList(",", ";");
        configBuilder.setSeparator(DEFAULT_SEP);
        choiceBoxSep.setValue(DEFAULT_SEP);
        choiceBoxSep.setItems(options);
        choiceBoxSep.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    configBuilder.setSeparator(newValue);
                    String absInputFilePath = getAbsInputFilePath();
                    if (!absInputFilePath.isEmpty()) {
                        reloadPreview(absInputFilePath, newValue);
                    }
                });
        textHeightField.setText(DEFAULT_TEXT_HEIGHT);
        dimensionGroup = new ToggleGroup();
        convertButton.disableProperty()
                .bind(Bindings.isEmpty(inputTextField.textProperty())
                        .or(Bindings.isEmpty(outputTextField.textProperty())
                                .or(Bindings.isEmpty(textHeightField.textProperty()))
                        ));
        convertButton.setOnAction(this::convert);

    }

    public void setMainApp(Main mainApp) {
        this.main = mainApp;
    }

    private void openAbout(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About CSV to DXF");
        alert.setHeaderText(null);
        alert.setContentText("CSV to DXF converts a a list of comma separated survey data to a DXF file." +
                " The survey data has to have at least a point id and a pair of coordinates.\nby Laszlo Bogacsi\n@2018");

        alert.showAndWait();
    }

    private void browseInput(ActionEvent e) {
        Stage stage = (Stage) rootVBox.getScene().getWindow();
        FileChooser inputFilechooser = new FileChooser();
        // show only csv files
        FileChooser.ExtensionFilter csvExtFilter = new FileChooser.ExtensionFilter("*.csv, *.txt files", "*.txt, *.csv");
        inputFilechooser.getExtensionFilters().add(csvExtFilter);
        File selectedFile = inputFilechooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String absInputFilePath = selectedFile.getPath();
            inputTextField.setText(selectedFile.getName());
            configBuilder.setInputPath(Paths.get(absInputFilePath));
            reloadPreview(absInputFilePath, choiceBoxSep.getValue());
            setAbsInputFilePath(absInputFilePath);
        }
    }

    private void browseOutput(ActionEvent e) {
        Stage stage = (Stage) rootVBox.getScene().getWindow();
        FileChooser outputFilechooser = new FileChooser();
        // save only dxf files
        FileChooser.ExtensionFilter csvExtFilter = new FileChooser.ExtensionFilter("*.dxf files", "*.dxf");
        outputFilechooser.getExtensionFilters().add(csvExtFilter);
        File selectedFile = outputFilechooser.showSaveDialog(stage);
        if (selectedFile != null) {
            String absInputFilePath = selectedFile.getPath();
            outputTextField.setText(selectedFile.getName());
            configBuilder.setOutputPath(Paths.get(absInputFilePath));
        }
    }

    private void convert(ActionEvent e) {
        ConversionReport report = new ConversionReport();
        final Task task;
        convertButton.setVisible(false);
        convertButton.setManaged(false);
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                progressIndicator.setVisible(true);
                Converter converter = new CsvToDxf(reader, report);
                converter.convert(assembleConfig());
                return null;
            }

            @Override
            protected void succeeded() {
                progressIndicator.setVisible(false);
                convertButton.setManaged(true);
                convertButton.setVisible(true);
                main.showConvertResultDialog(report);
            }

            @Override
            protected void failed() {
                progressIndicator.setVisible(false);
                convertButton.setManaged(true);
                convertButton.setVisible(true);
                Throwable t = this.getException();
                showAlertDialog(t);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void showAlertDialog(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("CSV to DXF - ERROR");
        alert.setHeaderText("An error occured");
        alert.setContentText(t.toString());
        alert.showAndWait();
    }

    private DrawingConfig assembleConfig() {
        configBuilder.setTextHeight(parseStringInputToDouble(textHeightField.getText()));
        configBuilder.setDoPrintId(pointNumberCheckBox.isSelected());
        configBuilder.setDoPrintHeight(heightCheckBox.isSelected());
        configBuilder.setDoPrintCoords(coordinatedCheckBox.isSelected());
        configBuilder.setDoPrintCode(codeCheckBox.isSelected());
        configBuilder.setIs3D(is3DButton.isSelected());
        configBuilder.setLayerByCode(layersByCodeCheckbox.isSelected());
        return configBuilder.build();
    }

    /**
     * Creates a read only table to preview the first n lines of the input file
     *
     * @param path to the input file, String, absolute path
     */
    private void reloadPreview(String path, String separator) {
        // clear table before reload
        previewTable.getItems().clear();
        List<CsvLine> previewLines = reader.readLine(Paths.get(path), separator, PREVIEW_LIST_LENGTH);
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
        return Double.parseDouble(input.replaceAll(",", ".").replaceAll("[^0-9.]+", ""));
    }
}
