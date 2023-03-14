package com.example.minesweeper;

import java.lang.String;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static int FIELD_SIZE = 30;
    static int[][] CONSTRAINTS = {{9,11,120,180,0},{35,45,240,360,1}};
    static StringProperty loadedScenario = new SimpleStringProperty("(no scenario)");
    static Round loadedRound;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Main.fxml"));
        primaryStage.setTitle("MediaLab Minesweeper");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}