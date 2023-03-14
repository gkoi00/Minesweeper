package com.example.minesweeper;

import java.util.ArrayList;


public class Round {
    private int[] parameters = {0,0,0,0}; // difficulty, mines, totalTime, superMine
    private String scenario;
    private int gridDimensions;
    private int endTime = 0;
    private int endTries = 0;
    private boolean won;
    private boolean ended = false;
    public int openedPieces = 0;
    public Piece[][] piecesArray;
    static Round currRound;
    static ArrayList<Round> history = new ArrayList<>();

    public Round(int difficulty, int mines, int totalTime, int superMine, String scenario){
        parameters = new int[]{difficulty, mines, totalTime, superMine};
        gridDimensions = (parameters[0] == 1) ? 9 : 16;
        this.scenario = scenario;
    }

    public void launch(){
        currRound = this;
        piecesArray = new Piece[gridDimensions][gridDimensions];
        for (int i = 0; i < gridDimensions; i++){
            for (int j = 0; j< gridDimensions; j++){
                piecesArray[i][j] = new Piece(i,j);
            }
        }
    }
    public Round reGenerate(){
        return new Round(getDifficulty(), getMines(), getTotalTime(), parameters[3], scenario);
    }
    public void end(int timeLeft, boolean won){
        this.endTime = getTotalTime() - timeLeft;
        this.won = won;
        history.add(0, this);
        ended = true;
    }
    public void addTry(){
        endTries++;
    }

    public int getDifficulty() {
        return parameters[0];
    }
    public int getMines() {
        return parameters[1];
    }
    public int getTotalTime() {
        return parameters[2];
    }
    public boolean hasSuperMine() {
        return (parameters[3] == 1);
    }
    public int getGridDimensions() {
        return gridDimensions;
    }
    public String getScenario() {
        return scenario;
    }
    public int getEndTime() {
        return endTime;
    }
    public int getEndTries() {
        return endTries;
    }
    public boolean getWon() { return won; }
    public boolean isEnded() { return ended; }
}
