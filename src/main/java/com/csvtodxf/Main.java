package com.csvtodxf;

import com.csvtodxf.ui.Controller;
import com.csvtodxf.ui.ConvertResultDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    Stage mainStage;
    // TODO: TESTING!!!!

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/csvtodxf/ui/main.fxml"));
        Parent root = loader.load();
        mainStage.setTitle("CSV to DXF");
        mainStage.setMinWidth(500);
        mainStage.setMinHeight(560);
        mainStage.setMaxWidth(650);
        mainStage.setMaxHeight(650);
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/com/csvtodxf/ui/main.css").toString());
        mainStage.setScene(scene);
        Controller controller = loader.getController();
        controller.setMainApp(this);
        mainStage.show();

    }

    public void showConvertResultDialog(ConversionReport report) {
        try {
            // Load the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/csvtodxf/ui/convertResultDialog.fxml"));
            GridPane dialogPane = loader.load();

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Result");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainStage);
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            // Set stage and report into the controller
            ConvertResultDialog resultDialogController = loader.getController();
            resultDialogController.setDialogStage(dialogStage);
            resultDialogController.setReport(report);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}
