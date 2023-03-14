package com.example.minesweeper;

import java.util.ArrayList;
import java.util.Random;

import static com.example.minesweeper.Round.currRound;


public class FieldSetUp {

    static ArrayList<Piece> getNeighbours(int x, int y){
        int gridDimensions = currRound.getGridDimensions();
        Piece[][] piecesArray = currRound.piecesArray;
        ArrayList<Piece> neighboursList = new ArrayList<>();
        if (x < gridDimensions -1) {
            neighboursList.add(piecesArray[x+1][y]);
            if (y < gridDimensions -1)
                neighboursList.add(piecesArray[x+1][y+1]);
        }
        if (y < gridDimensions -1)
            neighboursList.add(piecesArray[x][y+1]);

        if (x>0){
            neighboursList.add(piecesArray[x-1][y]);
            if (y < gridDimensions -1)
                neighboursList.add(piecesArray[x-1][y+1]);
            if (y>0)
                neighboursList.add(piecesArray[x-1][y-1]);
        }
        if (y>0){
            if (x < gridDimensions -1)
                neighboursList.add(piecesArray[x+1][y-1]);
            neighboursList.add(piecesArray[x][y-1]);
        }
        return neighboursList;
    }

    static int countNeighboursMines(int x, int y){
        ArrayList<Piece> neighboursList = new ArrayList<>();
        neighboursList = getNeighbours(x, y);

        int MinesCount = 0;

        for (Piece p: neighboursList) {
            if (p.isMine()) MinesCount++;
        }

        return MinesCount;
    }

    static void revealNeighbours(int x, int y){
        ArrayList<Piece> neighboursList = new ArrayList<>();
        neighboursList = getNeighbours(x, y);
        for (Piece p: neighboursList) {
            if (p.getIsRevealed()) continue;
            p.reveal();
        }
    }
    static void superReveal(int x, int y){
        Piece[][] piecesArray = currRound.piecesArray;
        int gridDimensions = currRound.getGridDimensions();
        for (int i = 0; i < gridDimensions; i++) {
            if (i == y) continue;
            if (piecesArray[x][i].isMine()) piecesArray[x][i].specialRevealMine();
            else piecesArray[x][i].revealNotReccursive();
        }
        for (int i = 0; i < gridDimensions; i++) {
            if (i == x) continue;
            if (piecesArray[i][y].isMine()) piecesArray[i][y].specialRevealMine();
            else piecesArray[i][y].revealNotReccursive();
        }
    }

    static void generateMines(int x, int y){
        // generate Mines away from x, y
        Random rand = new Random();
        int totalMines = currRound.getMines();
        int gridDimensions = currRound.getGridDimensions();
        Piece[][] piecesArray = currRound.piecesArray;
        int superMineIndex = -1;
        if (currRound.hasSuperMine()) superMineIndex = rand.nextInt(totalMines - 1);
        
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int i = rand.nextInt(gridDimensions - 1);
            int j = rand.nextInt(gridDimensions - 1);
            if (Math.abs(x - i) <= 1 || Math.abs(y - j) <= 1) continue;
            if (piecesArray[i][j].isMine()) continue;
            // Make Mine
            if (minesPlaced == superMineIndex) piecesArray[i][j].makeSuperMine();
            else piecesArray[i][j].makeMine();
            minesPlaced++;
        }
        for (int i = 0; i < gridDimensions; i++){
            for (int j = 0; j< gridDimensions; j++){
                piecesArray[i][j].setNeighboursMinesCount(countNeighboursMines(i, j));
            }
        }
    }
}
