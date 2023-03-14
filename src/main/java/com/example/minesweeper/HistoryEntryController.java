package com.example.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HistoryEntryController {

    @FXML
    private GridPane grid;

    @FXML
    private Label mines;

    @FXML
    private Label time;

    @FXML
    private Label tries;

    @FXML
    private Label winner;

    public void setValues(int mines, int tries, int time, boolean won){
        this.mines.setText(String.valueOf(mines));
        this.tries.setText(String.valueOf(tries));
        this.time.setText(String.valueOf(time));
        String winner = (won) ? "Player" : "Computer";
        this.winner.setText(winner);
        if (won) grid.setStyle("-fx-background-color: #D0E1FF ;");
        else grid.setStyle("-fx-background-color: #FFCDD1 ;");
    }
}
