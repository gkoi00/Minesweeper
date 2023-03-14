package com.example.minesweeper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import static com.example.minesweeper.Main.FIELD_SIZE;
import static com.example.minesweeper.Round.currRound;

class Piece extends StackPane {
    private int x, y;
    private boolean isMine = false;
    private boolean isSuperMine = false;
    private boolean isRevealed = false;
    private int neighboursMinesCount;
    private ImageView tileImg;
    private ImageView flagImg;
    private ImageView mineImg;
    private ImageView numberImg;
    static IntegerProperty flaggedPieces = new SimpleIntegerProperty(0);
    static int tries = 0;

    public Piece(int x, int y){
        this.x = x;
        this.y = y;

        mineImg = new ImageView(new Image(getClass().getResource("images/mine.png").toString()));
        mineImg.setVisible(false);
        mineImg.fitWidthProperty().bind(this.widthProperty());
        mineImg.setPreserveRatio(true);

        tileImg = new ImageView(new Image(getClass().getResource("images/tile_hidden.png").toString()));

        numberImg = new ImageView();
        numberImg.setVisible(false);

        flagImg = new ImageView(new Image(getClass().getResource("images/flag.png").toString()));
        flagImg.setVisible(false);

        for (ImageView i : new ImageView[]{mineImg, tileImg, numberImg, flagImg}){
            i.fitWidthProperty().bind(this.widthProperty());
            i.fitHeightProperty().bind(this.heightProperty());
            i.setPreserveRatio(true);
        }



        this.getChildren().addAll(tileImg, mineImg, numberImg, flagImg);

        setOnMouseClicked(event -> check(event));

        setTranslateX(FIELD_SIZE * x);
        setTranslateY(FIELD_SIZE * y);
    }

    void check(MouseEvent e){
        System.out.println(currRound.openedPieces);
        if (currRound.isEnded()) {
            this.setDisable(true);
            return;
        }

        if (isRevealed) return;

        if ((e.getButton() == MouseButton.SECONDARY)){
            boolean isFlagged = this.flagImg.visibleProperty().getValue();
            if (flaggedPieces.getValue() >= currRound.getMines() && !isFlagged) return;
            this.flagImg.setVisible(!isFlagged);
            if (isFlagged){
                flaggedPieces.set(flaggedPieces.get()-1);
            }
            else {
                flaggedPieces.set(flaggedPieces.get()+1);
            }
            if (isSuperMine && currRound.getEndTries() <= 4){
                FieldSetUp.superReveal(x, y);
            }
        }
        else{
            // First Cell
            if (currRound.openedPieces == 0){
                FieldSetUp.generateMines(x, y);
            }
            if(isMine){
                revealMine();
                MainController.endGame(false);
                return;
            }
            reveal();
            currRound.addTry();

        }
    }

    public void reveal(){
        revealNotReccursive();
        if (this.neighboursMinesCount == 0) FieldSetUp.revealNeighbours(x, y);
    }

    public void specialRevealMine(){
        if (this.flagImg.visibleProperty().getValue()==true) flaggedPieces.set(flaggedPieces.get()-1);
        this.flagImg.setVisible(false);
        this.tileImg.setImage(new Image(getClass().getResource("images/revealed_mine.png").toString()));
        this.setDisable(true);
    }
    public void revealMine(){
        mineImg.setVisible(true);
        tileImg.setImage(new Image(getClass().getResource("images/tile_revealed.png").toString()));
        flagImg.setVisible(false);
    }


    //MAKE
    public void revealNotReccursive(){
        if (isRevealed) return;
        isRevealed = true;
        currRound.openedPieces++;
        // Check if game ended
        int gridDimensions = currRound.getGridDimensions();
        int totalMines = currRound.getMines();
        if(currRound.openedPieces == (gridDimensions * gridDimensions)- totalMines){
            MainController.endGame(true);
        }
        if (this.flagImg.visibleProperty().getValue()==true) {
            flaggedPieces.set(flaggedPieces.get()-1);
            this.flagImg.setVisible(false);
        }
        this.tileImg.setImage(new Image(getClass().getResource("images/tile_revealed.png").toString()));
        if (this.neighboursMinesCount != 0) {
            this.numberImg.setImage(new Image(getClass().getResource("images/number_" + String.valueOf(this.neighboursMinesCount) + ".png").toString()));
            this.numberImg.setVisible(true);
        }
    }

    static void reset(){
        flaggedPieces.set(0);
    }

    public void makeMine(){
        this.isMine = true;
    }
    public void makeSuperMine(){
        this.isMine = true;
        this.isSuperMine = true;
    }
    public void setNeighboursMinesCount(int count){
        this.neighboursMinesCount = count;
    }
    public boolean isMine(){
        return this.isMine;
    }
    public boolean getIsRevealed(){
        return this.isRevealed;
    }
}