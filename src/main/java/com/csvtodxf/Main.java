package com.csvtodxf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/com/csvtodxf/ui/main.fxml"));
        primaryStage.setTitle("CSV to com.csvtodxf.DXF");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(560);
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/com/csvtodxf/ui/main.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
