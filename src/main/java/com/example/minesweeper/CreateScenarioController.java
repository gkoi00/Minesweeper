package com.example.minesweeper;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.example.minesweeper.Main.CONSTRAINTS;

public class CreateScenarioController {

    @FXML
    private Button createButton;

    @FXML
    private ChoiceBox<Integer> difficulty;

    @FXML
    private Slider mines;

    @FXML
    private TextField scenarioID;

    @FXML
    private CheckBox superMine;

    @FXML
    private Slider time;
    @FXML
    private Text selectedMines;

    @FXML
    private Text selectedTime;

    @FXML
    public void initialize() {
        difficulty.getItems().addAll(1,2);
        difficulty.getSelectionModel().selectedItemProperty().addListener((observable, number, selected) -> {
                    if (selected.equals(1)) {
                        mines.setMin(CONSTRAINTS[0][0]);
                        mines.setMax(CONSTRAINTS[0][1]);
                        time.setMin(CONSTRAINTS[0][2]);
                        time.setMax(CONSTRAINTS[0][3]);
                        mines.setShowTickLabels(true);
                        mines.setDisable(false);
                        selectedMines.setVisible(true);
                        time.setShowTickLabels(true);
                        time.setDisable(false);
                        selectedTime.setVisible(true);
                        superMine.setSelected(false);
                        superMine.setDisable(true);
                    } else if (selected.equals(2)) {
                        mines.setMin(CONSTRAINTS[1][0]);
                        mines.setMax(CONSTRAINTS[1][1]);
                        time.setMin(CONSTRAINTS[1][2]);
                        time.setMax(CONSTRAINTS[1][3]);
                        mines.setShowTickLabels(true);
                        mines.setDisable(false);
                        selectedMines.setVisible(true);
                        time.setShowTickLabels(true);
                        time.setDisable(false);
                        selectedTime.setVisible(true);
                        superMine.setSelected(false);
                        superMine.setDisable(false);
                    }
                });
        mines.valueProperty().addListener((observable, number, selected) -> {
                selectedMines.setText(String.valueOf(selected.intValue()));
        });
        time.valueProperty().addListener((observable, number, selected) -> {
                selectedTime.setText(String.valueOf(selected.intValue()));
        });
    }

    @FXML
    void GenerateNewScenario(ActionEvent event) throws IOException {
        if (scenarioID.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a scenario id.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (difficulty.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select difficulty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Writer scenario = new FileWriter("medialab/"+ scenarioID.getText() +".txt", false);
        String superMineStr = (superMine.isSelected()) ? "1" : "0";
        String txt = String.format("%d\n%s\n%s\n%s",difficulty.getValue(),selectedMines.getText(),selectedTime.getText(),superMineStr);
        scenario.write(txt);
        scenario.close();
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}
