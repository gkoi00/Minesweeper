package com.example.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

import static com.example.minesweeper.Main.*;

public class LoadScenarioController {

    @FXML
    private Button loadButton;

    @FXML
    private TextField scenarioID;

    @FXML
    void LoadScenario(ActionEvent event) {
        int difficulty;
        int mines;
        int time;
        int superMine;
        try {
            FileReader f = new FileReader("medialab/"+ scenarioID.getText() +".txt");
            BufferedReader reader = new BufferedReader(f);

            String line;
            String file = "";
            while ((line = reader.readLine()) != null) {
                file = file + line + "\n";
            }
            f.close();

            if (!file.matches("([0-9]+\n){4}")){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid scenario file.", ButtonType.OK);
                alert.showAndWait();
            }

            String[] params = file.split("\n",-1);
            difficulty =  Integer.valueOf(params[0]);
            mines =  Integer.valueOf(params[1]);
            time =  Integer.valueOf(params[2]);
            superMine = Integer.valueOf(params[3]);

            if (!IsValid(difficulty,mines,time,superMine)){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid scenario file.", ButtonType.OK);
                alert.showAndWait();
            }

            // Update Loaded Scenario
            loadedScenario.setValue(String.format("(%s)", scenarioID.getText()));

            // Generate New Round
            loadedRound = new Round(difficulty, mines, time, superMine, loadedScenario.getValue());


            Stage stage = (Stage) loadButton.getScene().getWindow();
            stage.close();
        }
        catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Scenario not found.", ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private boolean IsValid(int difficulty, int mines, int time, int superMine){
        boolean case1 = (difficulty == 1) && (mines >= CONSTRAINTS[0][0] && mines <= CONSTRAINTS[0][1]) &&
                (time >= CONSTRAINTS[0][2] && time <= CONSTRAINTS[0][3]) && (superMine == CONSTRAINTS[0][4]);
        boolean case2 = (difficulty == 2) && (mines >= CONSTRAINTS[1][0] && mines <= CONSTRAINTS[1][1]) &&
                (time >= CONSTRAINTS[1][2] && time <= CONSTRAINTS[1][3]) && (superMine == 0 || superMine == 1);
        return (case1 || case2);
    }

}
