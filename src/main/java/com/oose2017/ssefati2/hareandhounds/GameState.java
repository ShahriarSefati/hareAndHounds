package com.oose2017.ssefati2.hareandhounds;

import java.util.UUID;
import com.google.gson.Gson;

public class GameState {

    private String state;
    private transient String whoseTurn;
    private transient String  gameId;
    private transient GamePlayer p1, p2;

    public GameState(String pieceType) {
        this.state = "WAITING_FOR_SECOND_PLAYER";
        this.whoseTurn = "HOUND";

        String uniqueGameId = UUID.randomUUID().toString();
        String uniquePlayerId = UUID.randomUUID().toString();

        // set up first player
        p1 = new GamePlayer(uniqueGameId);

        this.p1.setPieceType(pieceType);
        this.p1.setPlayerId(uniquePlayerId);
        this.gameId = p1.getGameId();
    }

    public String getState() {
        return this.state;
    }

    public String getGameId() { return this.gameId; }

    public String getTurn() {
        return this.whoseTurn;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTurn(String turn) {
        this.whoseTurn = turn;
    }


    // change turns
    public void changeTurn() {
        if (this.getTurn().equals("HOUND")) {
            setTurn("HARE");
            setState("TURN_HARE");
        } else {
            setTurn("HOUND");
            setState("TURN_HOUND");
        }
    }

    // Add a new player
    public GamePlayer addPlayer() {
        this.p2 = new GamePlayer(this.getGameId());
        String uniquePlayerId = UUID.randomUUID().toString();
        this.p2.setPlayerId(uniquePlayerId);

        // set player two's pieceType
        if (this.p1.getPieceType().equals("HOUND")) {
            p2.setPieceType("HARE");
        } else {
            p2.setPieceType("HOUND");
        }
        return this.p2;
    }

    public String getTurnPlayerId() {
        if (this.p1.getPieceType().equals(this.whoseTurn)) {
            return this.p1.getPlayerId();
        }
        else
            return this.p2.getPlayerId();
    }

    public String getFirstPlayerId(){
        return this.p1.getPlayerId();
    }

    public String getSecondPlayerId(){
        return this.p2.getPlayerId();
    }

    public GamePlayer getFirstPlayer() {
        return this.p1;
    }
    public GamePlayer getSecondPlayer() {
        return this.p2;
    }

}
