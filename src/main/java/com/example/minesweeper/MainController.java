package com.example.minesweeper;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static com.example.minesweeper.Main.*;
import static com.example.minesweeper.Round.currRound;

public class MainController {

    @FXML
    private Label startItemSubtext;

    @FXML
    private Pane grid;

    @FXML
    private HBox topBar;
    @FXML
    private VBox background;
    @FXML
    private Text starupMessage;
    @FXML
    private Label flaggedCells;
    @FXML
    private Label time;
    @FXML
    private Label totalMines;
    static int remainingTime;
    static Timeline timerTimeline = new Timeline();

    @FXML
    public void initialize() {
        startItemSubtext.textProperty().bind(loadedScenario);
        Background back = new Background(new BackgroundFill(Color.web("#D4D0C8"), null, null));
        background.setBackground(back);
        starupMessage.setText("Please Create/Load a Scenario...");
    }
    @FXML
    void CreateScenarioWindow(ActionEvent event) throws Exception{
        Stage popupStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/CreateScenario.fxml"));
        popupStage.setTitle("New Scenario");
        popupStage.setResizable(false);
        popupStage.setScene(new Scene(root));
        popupStage.show();
    }

    @FXML
    void ExitApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void LoadScenario(ActionEvent event) throws Exception{
        Stage popupStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/LoadScenario.fxml"));
        popupStage.setTitle("Load Scenario");
        popupStage.setResizable(false);
        popupStage.setScene(new Scene(root));
        popupStage.show();
    }

    @FXML
    void ShowRoundHistory(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/RoundHistory.fxml"));
        popupStage.setTitle("Round History");
        popupStage.setResizable(false);
        popupStage.setScene(new Scene(root));
        popupStage.show();
    }

    @FXML
    void ShowSolution(ActionEvent event) {
        // If no round doesn't exist or has ended
        if (currRound == null) return;
        if (currRound.isEnded()) return;
        // If not generated yet
        if (currRound.openedPieces == 0){
            FieldSetUp.generateMines(-2,-2);
        }

        Piece[][] piecesArray = currRound.piecesArray;
        for (Piece[] pieces: piecesArray){
            for (Piece piece: pieces){
                if (piece.isMine()) {
                    piece.revealMine();
                }
            }
        }
        endGame(false);
    }

    @FXML
    void StartNewGame(ActionEvent event) {
        //resetpieceopen
        if (loadedRound == null) return;
        // Reset
        Piece.reset();
        grid.getChildren().clear();
        timerTimeline.stop();

        // Launch Round
        loadedRound = loadedRound.reGenerate();
        loadedRound.launch();

        topBar.setVisible(true);
        starupMessage.setVisible(false);

        // Generate Grid
        int gridDimensions = currRound.getGridDimensions();
        grid.setPrefSize(gridDimensions * FIELD_SIZE, gridDimensions * FIELD_SIZE);
        Stage stage = (Stage) grid.getScene().getWindow();
        stage.sizeToScene();
        Piece[][] piecesArray = currRound.piecesArray;
        for (Piece[] pieces: piecesArray){
            for (Piece piece: pieces){
                grid.getChildren().add(piece);
            }
        }

        // TopBar Items
        Round currRound = Round.currRound;
        totalMines.setText(Integer.toString(currRound.getMines()));
        Piece.flaggedPieces.addListener((observable, oldValue, newValue) -> {
            flaggedCells.setText(Integer.toString(Piece.flaggedPieces.get()));
        });
        remainingTime = currRound.getTotalTime();
        time.setText(Integer.toString(remainingTime));
        timerTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event1 -> {
                    remainingTime--;
                    time.setText(Integer.toString(remainingTime));
                    if (remainingTime == 0) endGame(false);
                })
        );
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();

        // Styling of TopBar
        for (Node label : topBar.getChildren()) {
            label.setStyle("-fx-font-family: Verdana; -fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white; -fx-stroke: whitesmoke; -fx-stroke-width: 1px; -fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);");
        }
    }
    static void endGame(Boolean won){
        // Pop Up Window
        Stage gameEndPopup = new Stage();
        Text gameEndTxt;
        gameEndPopup.setResizable(false);
        if (!won){
            gameEndPopup.setTitle("Defeat");
            gameEndTxt = new Text("You Lost!");
        }
        else{
            gameEndPopup.setTitle("Victory");
            gameEndTxt = new Text("You Won!");
        }
        gameEndTxt.setVisible(true);
        BorderPane popupPane = new BorderPane();
        popupPane.setCenter(gameEndTxt);
        gameEndPopup.setScene(
                new Scene(popupPane, 250, 100));
        gameEndPopup.show();

        loadedRound = loadedRound.reGenerate();
        currRound.end(remainingTime, won);
        timerTimeline.stop();
    }
}
