package com.oose2017.ssefati2.hareandhounds;

public class GamePieces {

//    private String gameId;
    private String pieceType;
    private int x;
    private int y;

    public GamePieces(String pieceType, int x, int y){
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
    }

    public void updatePos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String getPieceType(){ return pieceType; };

    public boolean sitsAt(int atX, int atY){
        return (this.getX() == atX && this.getY() == atY);
    }

    public int getX(){ return x; };

    public int getY(){ return y; };

    public void setX(int x){ this.x = x;}

    public void setY(int y){ this.y = y;}

}
