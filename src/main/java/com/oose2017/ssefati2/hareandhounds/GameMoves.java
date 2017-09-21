package com.oose2017.ssefati2.hareandhounds;

public class GameMoves {

    private String playerId;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public GameMoves(String playerId, int fromX, int fromY, int x, int y){
        this.playerId = playerId;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = x;
        this.toY = y;
    }

    // class get methods
    public String getPlayerId(){ return playerId; };

    public int getToX(){ return toX; };

    public int getToY(){ return toY; };

    public int getFromX(){ return fromX; };

    public int getFromY(){ return fromY; };


}
