package com.example.minesweeper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RoundHistoryController {

    @FXML
    private GridPane grid;
    @FXML
    private GridPane pastRound;

    @FXML
    public void initialize() throws IOException {
        for (int i = 0; i < 5; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/HistoryEntry.fxml"));
            GridPane roundGrid = loader.load();
            HistoryEntryController controller = loader.getController();
            if (Round.history.size() > i) {
                Round round = Round.history.get(i);
                controller.setValues(round.getMines(), round.getEndTries(), round.getEndTime(), round.getWon());
            }
            grid.add(roundGrid, 0, i);
        }
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) grid.getScene().getWindow();
        stage.close();
    }

}
