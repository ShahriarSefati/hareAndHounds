package com.oose2017.ssefati2.hareandhounds;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameService {

    private HashMap<String , GameState> gamesList = new HashMap<String, GameState>();
    private HashMap<String, GameBoard> boardsList = new HashMap<String, GameBoard>();

    private final Logger logger = LoggerFactory.getLogger(GameService.class);


    public GameService() {
    }

    // Create new game
    public GamePlayer createNewGame(String body) throws GameServiceException {
        try{
            // initialize gameState and gameBoard
            PieceType pieceType = new Gson().fromJson(body, PieceType.class);

//            // check for invalid pieceType
            if (!(pieceType.getPieceType().equals("HOUND")) && !(pieceType.getPieceType().equals("HARE"))) {
                throw new GameServiceException("GameService.createNewGame: Improper pieceType:",1);
            }

            GameState gameState = new GameState(pieceType.getPieceType());
//            System.out.printf("game Id from state SERVICE: %s\n", gameState.getGameId());
//            System.out.printf("player Id from state SERVICE: %s\n", gameState.getFirstPlayerId());
            GameBoard gameBoard = new GameBoard(gameState.getGameId());
//            System.out.printf("game Id from board SERVICE: %s\n", gameBoard.getGameId());

            // store gameState and gameBoard (and their gameId's) in gamesList and boardsList
            gamesList.put(gameState.getGameId(), gameState);
            boardsList.put(gameState.getGameId(), gameBoard);
//            System.out.printf("game Id from player SERVICE: %s\n", gamesList.get(gameState.getGameId()).getFirstPlayer().getGameId());
//            System.out.printf("player Id from player SERVICE: %s\n", gamesList.get(gameState.getGameId()).getFirstPlayer().getPlayerId());
//            System.out.printf("player Id from player SERVICE (diff): %s\n", gamesList.get(gameState.getGameId()).getFirstPlayerId());
//            System.out.printf("type from player SERVICE: %s\n", gamesList.get(gameState.getGameId()).getFirstPlayer().getPieceType());
            return gamesList.get(gameState.getGameId()).getFirstPlayer();
        } catch(Exception ex) {
            logger.error("GameService.createNewGame: Failed to create new game", ex);
            throw new GameServiceException("GameService.createNewGame: Failed to create new game", ex);
        }
    }

    // Play game
    public String playGame(String gameId, String body) throws GameServiceException{
        GameMoves gameMoves = new Gson().fromJson(body, GameMoves.class);
        String playerId = gameMoves.getPlayerId();
        int fromX = gameMoves.getFromX();
        int fromY = gameMoves.getFromY();
        int toX = gameMoves.getToX();
        int toY = gameMoves.getToY();

        if (gamesList.get(gameId) == null) {
            throw new GameServiceException(String.format("GameService.playGame: invalid game id: %s", gameId), 1);
        }

        if (!(gamesList.get(gameId).getFirstPlayerId().equals(playerId)) &&
                !(gamesList.get(gameId).getSecondPlayerId().equals(playerId))) {
            throw new GameServiceException(String.format("GameService.playGame: Invalid player id: %s", playerId), 2);
        }

        if(!(gamesList.get(gameId).getTurnPlayerId().equals(playerId))) {
                throw new GameServiceException("GameService.playGame: It's not your turn!", 3);
        }

        if(gamesList.get(gameId).getState().equals("WAITING_FOR_SECOND_PLAYER")) {
            throw new GameServiceException("GameService.playGame: Waiting for second player!", 3);
        }

        // check illegal movements
        // movement after winning situation
        if (gamesList.get(gameId).getState().equals("WIN_HARE_BY_STALLING") ||
                gamesList.get(gameId).getState().equals("WIN_HARE_BY_ESCAPE") ||
                gamesList.get(gameId).getState().equals("WIN_HOUND")){
            throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // illegal large step movements (both hare and hounds)
        if (Math.abs(fromX - toX) > 1 || Math.abs(fromY - toY) > 1){
            throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // illegal disconnected cell movements (both hare and hounds)
        if (Math.abs(fromX - toX) <= 1 && Math.abs(fromY - toY) > 1){
            throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // illegal disconnected cell movements (both hare and hounds)
        if (Math.abs(fromY - toY) <= 1 && Math.abs(fromX - toX) > 1){
            throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // illegal disconnected cell movements (both hare and hounds)
        if (areDisconnected(fromX, fromY, toX, toY)){
            throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // illegal movements of hound
        if (gamesList.get(gameId).getTurn().equals("HOUND")) {
            if (fromX > toX) {
                throw new GameServiceException("GameService.playGame: Illegal move:", 4);
            }
        }

        // illegal move to occupied slot
        if (boardsList.get(gameId).isOccupied(toX, toY)) {
                throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // illegal move to out of bounds x and y
        if (boardsList.get(gameId).isOutOfBound(toX, toY)) {
            throw new GameServiceException("GameService.playGame: Illegal move:", 4);
        }

        // update the board status for appropriate gameId
        boardsList.get(gameId).whichPiece(fromX, fromY).updatePos(toX, toY);
        boardsList.get(gameId).updateBoard();
        gamesList.get(gameId).changeTurn();

        // check for winner
        String state = boardsList.get(gameId).checkWinner();
        if (!(state.equals("No Winner"))) {
            gamesList.get(gameId).setState(state);
        }
        return playerId;
    }

    public boolean areDisconnected(int fromX, int fromY, int toX, int toY){
        if(fromX == 2 && fromY == 0 && toX == 1 && toY == 1) {return true;}
        if(fromX == 1 && fromY == 1 && toX == 2 && toY == 0) {return true;}
        if(fromX == 2 && fromY == 0 && toX == 3 && toY == 1) {return true;}
        if(fromX == 3 && fromY == 1 && toX == 2 && toY == 0) {return true;}
        if(fromX == 2 && fromY == 2 && toX == 1 && toY == 1) {return true;}
        if(fromX == 1 && fromY == 1 && toX == 2 && toY == 2) {return true;}
        if(fromX == 2 && fromY == 2 && toX == 3 && toY == 1) {return true;}
        if(fromX == 3 && fromY == 1 && toX == 2 && toY == 2) {return true;}

        return false;
    }
    // Join a game as second player
    public GamePlayer joinGame(String gameId) throws GameServiceException {
        if (gamesList.get(gameId) == null) {
            throw new GameServiceException(String.format("GameService.joinGame: invalid game id: %s", gameId), 1);
        } else if (!(gamesList.get(gameId).getState().equals("WAITING_FOR_SECOND_PLAYER"))) {
            throw new GameServiceException("GameService.joinGame: Second player already joined", 2);
        }
        gamesList.get(gameId).setState("TURN_HOUND");
        return gamesList.get(gameId).addPlayer();
    }

    // Get the current game board information
    public List<GamePieces> getBoardState(String gameId) throws GameServiceException{
        try {
            return boardsList.get(gameId).getBoardState();
        } catch (Exception ex){
            logger.error(String.format("GameService.getBoardState: game id does not exist: %s", gameId), ex);
            throw new GameServiceException(String.format("GameService.getBoardState: game id does not exist: %s", gameId), ex);
        }
    }

    // Get the game status (turn and winning status)
    public GameState getGameState(String gameId) throws GameServiceException{
        try {
            return gamesList.get(gameId);
        } catch (Exception ex) {
            logger.error(String.format("GameService.getGameState: game id does not exist: %s", gameId), ex);
            throw new GameServiceException(String.format("GameService.getGameState: game id does not exist: %s", gameId), ex);
        }
    }

    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    public static class GameServiceException extends Exception {
        private int statusCode;

        // constructors
        public GameServiceException(String message, Throwable cause) {
            super(message, cause);
        }
        public GameServiceException(String message, int code){
            super(message);
            this.statusCode = code;
        }

        public int getStatusCode() {
            return this.statusCode;
        }
    }
}
