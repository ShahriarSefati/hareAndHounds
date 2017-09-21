package com.oose2017.ssefati2.hareandhounds;

public class GamePlayer {

    private String pieceType;
    private transient String gameId;
    private transient String playerId;


    public GamePlayer(String gameId){
        this.gameId = gameId;
    }

    // class get methods
    public String getGameId(){ return gameId; };

    public String getPlayerId(){ return playerId; };

    public String getPieceType(){ return pieceType; };

    // class set methods
    public void setGameId(String gameId){ this.gameId = gameId; };

    public void setPlayerId(String playerId){ this.playerId = playerId; };

    public void setPieceType(String pieceType){ this.pieceType = pieceType; };


}
