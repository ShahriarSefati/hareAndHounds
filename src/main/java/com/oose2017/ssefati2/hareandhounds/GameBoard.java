package com.oose2017.ssefati2.hareandhounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class GameBoard {

    // board history (list of all the board states in one game)
    private List<List<GamePieces>> boardHistory;
    private Map<String, Integer> pieceLocation;

    private String gameId;
    private GamePieces hare;
    private GamePieces firstHound;
    private GamePieces secondHound;
    private GamePieces thirdHound;

    public GameBoard(String gameId){
        this.gameId = gameId;
        this.boardHistory= new ArrayList<List<GamePieces>>();
        this.pieceLocation = new HashMap<String, Integer>();
        initializeBoard();
    }

    // figure out which piece is moving
    public GamePieces whichPiece(int fromX, int fromY){
        if (hare.getX() == fromX && hare.getY() == fromY) {return hare;}
        else if (firstHound.getX() == fromX && firstHound.getY() == fromY) {return firstHound;}
        else if (secondHound.getX() == fromX && secondHound.getY() == fromY) {return secondHound;}
        else if (thirdHound.getX() == fromX && thirdHound.getY() == fromY) {return thirdHound;}
        return null;
    }

    // check for occupied slots
    public boolean isOccupied(int toX, int toY){
        if (hare.getX() == toX && hare.getY() == toY) {return true;}
        else if (firstHound.getX() == toX && firstHound.getY() == toY) {return true;}
        else if (secondHound.getX() == toX && secondHound.getY() == toY) {return true;}
        else if (thirdHound.getX() == toX && thirdHound.getY() == toY) {return true;}
        return false;
    }

    // check for out of bounds x or y
    public boolean isOutOfBound(int toX, int toY){
        if (toX < 0 || toX > 4 || toY < 0 || toY > 2) {return true;}
        if (toX == 0 && toY == 0) {return true;}
        if (toX == 0 && toY == 2) {return true;}
        if (toX == 4 && toY == 0) {return true;}
        if (toX == 4 && toY == 2) {return true;}
        return false;
    }

    // initialize gameBoard
    public void initializeBoard() {

        // initialize the board with known locations of hare and hounds
        hare = new GamePieces("HARE", 4, 1);
        firstHound = new GamePieces("HOUND", 1, 0);
        secondHound = new GamePieces("HOUND", 0, 1);
        thirdHound = new GamePieces("HOUND", 1, 2);

        updateBoard();
    }

    // update the pieces location
    public void updateBoard(){

        // populate the list of these piece objects
        List<GamePieces> boardState = new ArrayList<GamePieces>();
        boardState.add(hare);
        boardState.add(firstHound);
        boardState.add(secondHound);
        boardState.add(thirdHound);

        updateBoardHistory(boardState);
    }

    // store current board state into the board history
    public void updateBoardHistory(List<GamePieces> boardState){
        boardHistory.add(boardState);

        // store pieces coordinates
        int[] pos = new int[]{hare.getX(),hare.getY(),
                firstHound.getX(), firstHound.getY(),
                secondHound.getX(), secondHound.getY(),
                thirdHound.getX(), thirdHound.getY()};

        String coordinates = Arrays.toString(pos);

        // add coordinates to HashMap (Key: coordinates, Value: number of occurrence)
        if( !(pieceLocation.containsKey(coordinates))){
            pieceLocation.put(coordinates, 1);
        } else {
            pieceLocation.put(coordinates, pieceLocation.get(coordinates) + 1);
        }
    }

    // get current board state
    public List<GamePieces> getBoardState(){
        return boardHistory.get(boardHistory.size()-1);
    }

    // check for winner
    public String checkWinner(){

        // hare wins by scape
        if(hare.getX() <= firstHound.getX() &&
                hare.getX() <= secondHound.getX() &&
                hare.getX() <= thirdHound.getX()) {
            return "WIN_HARE_BY_ESCAPE";
        }

        // hare wins by stall
        if(pieceLocation.containsValue(3)) {return "WIN_HARE_BY_STALLING";}

        // hound wins
        // check for three slots where the hare might be trapped
        if(hare.sitsAt(2, 0) &&
                isOccupied(1, 0 ) &&
                isOccupied(2, 1 ) &&
                isOccupied(3, 0 )){
            return "WIN_HOUND";
        }

        if(hare.sitsAt(2, 2) &&
                isOccupied(1, 2 ) &&
                isOccupied(2, 1 ) &&
                isOccupied(3, 2 )){
            return "WIN_HOUND";
        }

        if(hare.sitsAt(4, 1) &&
                isOccupied(3, 0 ) &&
                isOccupied(3, 1 ) &&
                isOccupied(3, 2 )){
            return "WIN_HOUND";
        }
        return "No Winner";
    }
}
